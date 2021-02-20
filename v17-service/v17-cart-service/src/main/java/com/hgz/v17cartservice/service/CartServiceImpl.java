package com.hgz.v17cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ICartService;
import com.hgz.commons.base.ResultBean;
import com.hgz.pojo.CartItem;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/14
 */
@Service
public class CartServiceImpl implements ICartService {

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultBean add(String token, Long productId, int count) {
        //1.根据key查询到购物车信息
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        //三种存在情况(尽量不使用if-lese) 1.首次加入购物车   2.非首次加入已存在商品  3.非首次加入不存在购物车商品
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        //2.判断购物车是否存在--（是否是第一次加入购物车）
        //if (cart != null){
        if (cart.size() != 0){
            //判断此商品id已存在购物车中,调用haskey()放回的是boolean类型
            if (redisTemplate.opsForHash().hasKey(key.toString(),productId)) {
                //获取购物项对象
                CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
                //更改商品数量
                cartItem.setCount(cartItem.getCount()+count);
                //更新操作时间
                cartItem.setUpdateTime(new Date());
                //保存到redis中
                redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
                //返回结果
                return new ResultBean("200",true);
            }
        }
        //3.第一次操作 和 加入不存在购物车的商品（合起来）
        CartItem cartItem = new CartItem(productId,count,new Date());
        //添加购物项到redis中
        redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
        //设置有效期15天（根据实际需求而定天数，cookie也是非会话级）
        redisTemplate.expire(key.toString(),15, TimeUnit.DAYS);
        return new ResultBean("200",true);
    }

    @Override
    public ResultBean delete(String token, Long productId) {
        //1.拼接key
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        //2.删除，判断是否存在此商品
        Long delete = redisTemplate.opsForHash().delete(key.toString(), productId);
        if (delete == 1){
            return new ResultBean("200",true);
        }
        //3.不存在则返回结果
        return new ResultBean("404",false);
    }

    @Override
    public ResultBean update(String token, Long productId, int count) {
        //1.拼接key
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        //2.判断是否存在此商品记录
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key.toString(), productId);
        if (cartItem != null) {
            cartItem.setCount(count);  //更新数量
            cartItem.setUpdateTime(new Date());  //更新修改时间
            redisTemplate.opsForHash().put(key.toString(),productId,cartItem);
            return new ResultBean("200",true);
        }
        //3.不存在则返回结果
        return new ResultBean("404",false);
    }

    @Override
    public ResultBean query(String token) {
        //1.拼接key
        StringBuilder key = new StringBuilder("user:cart:").append(token);
        //2.根据key到redis中查询cartItem项(时间没排序的)
        Map<Object, Object> cart = redisTemplate.opsForHash().entries(key.toString());
        //3.将cartItem--->cartItemVO  //根据商品id查询商品信息（判断是否有redis缓存,80/20定律）
       if (cart.size() > 0){
           //获取到cartItem集合
           Collection<Object> values = cart.values();
           //存入treeSet,使得时间降序排序
           TreeSet<CartItem> cartSet = new TreeSet<>();
           for (Object value : values) {
               //values--treeSet:变为有序
               cartSet.add((CartItem) value);
           }
           //将set-->list
           List<CartItem> cartItemList = new ArrayList<>(cartSet);
           return new ResultBean("200",cartItemList);
       }
        return new ResultBean("404",null);
    }

    /**
     * 转为登录状态-合并购物车
     * @param no_login_Key  user:cart:no_loginKey 未登录状态凭证
     * @param login_Key   user:cart:loginKey   登录状态购物车唯一标识(不能为jwt标识)
     * @return
     */
    @Override
    public ResultBean merge(String no_login_Key, String login_Key) {
        //1.拼接key
        StringBuilder nologinkey = new StringBuilder("user:cart:").append(no_login_Key);
        StringBuilder loginkey = new StringBuilder("user:cart:").append(login_Key);
        //2.判断未登录状态下是否存在购物车
        Map<Object, Object> nologinCart = redisTemplate.opsForHash().entries(nologinkey.toString());
        if (nologinCart.size() == 0){
            //不存在，直接返回
            return new ResultBean("200","不必合并");
        }

        //3.判断登录状态是否存在购物车
        Map<Object, Object> loginCart = redisTemplate.opsForHash().entries(loginkey.toString());
        if (loginCart.size() == 0){
            //3.1将未登录状态购物车转为登录状态购物车
            redisTemplate.opsForHash().putAll(loginkey.toString(),nologinCart);
            //TODO 添加到mysql数据库
            //3.2删除未登录状态的购物车
            redisTemplate.delete(nologinkey.toString());
            return new ResultBean("200","merge-success");
        }

        //4.两种状态都存在购物车，以登录状态的购物车为主，需考虑商品相同数量相加,否则直接添加
        for (Map.Entry<Object,Object> nologinEntry : nologinCart.entrySet()){
            //nologinEntry.getKey()  //productId
            //nologinEntry.getValue()   //CartItem
            //判断商品是否已存在登录购物车中
            CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(loginkey.toString(), nologinEntry.getKey());
            if (cartItem != null){
                //存在，则商品数量相加
                CartItem noLoginCartItem = (CartItem) nologinEntry.getValue();
                cartItem.setCount(cartItem.getCount()+noLoginCartItem.getCount());
                cartItem.setUpdateTime(new Date());
                //数量更改后存入redis
                redisTemplate.opsForHash().put(loginkey.toString(),nologinEntry.getKey(),cartItem);
            }else {
                //不存在相同商品，则直接添加
                redisTemplate.opsForHash().put(loginkey.toString(),nologinEntry.getKey(),nologinEntry.getValue());
                //TODO 添加到mysql数据库
            }
        }
        //删除未登录的购物车
        redisTemplate.delete(nologinkey.toString());
        return new ResultBean("200","merge-success");
    }
}
