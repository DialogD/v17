package com.hgz.v17miaosha.service.impl;

import com.hgz.commons.constant.MQConstant;
import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.exception.MiaoShaException;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/20
 */
@Service
public class MiaoShaServiceImpl implements IMiaoShaService {

    @Autowired
    private TMiaoshaProductMapper miaoshaProductMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static int count = 0;

    @Override
    @Cacheable(value = "product",key = "#id")  //缓存
    //默认product::1-------对象信息
    public TMiaoshaProduct getById(Long id) {
       /* //1.查询该商品是否存在该对象
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get("product:" + id);
        //2.判断是否存在
        if (product == null){
            //若不存在，从数据库中查询
            product = miaoshaProductMapper.selectByPrimaryKey(id);
            //存入redis中
            redisTemplate.opsForValue().set("product:"+id,product);

        }
        return product;*/
       return miaoshaProductMapper.selectByPrimaryKey(id);
    }

    @Transactional  //事务注解,数据库版本
    public ResultBean killold(Long userId, Long productId) {

        //从数据库获取库存信息
        TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(productId);
        if (product.getCount()>0){  // T1  T2  T3
            product.setCount(product.getCount()-1);
            product.setUpdateTime(new Date());
            //TODO 记录抢到的用户信息和商品信息userId--productId
            //TODO 生成订单编号
            miaoshaProductMapper.updateByPrimaryKeySelective(product);
            System.out.println("已抢购到第..."+(++count));
            return new ResultBean("200","秒杀成功~");
        }
        return new ResultBean("404","秒杀失败~");
    }

    public ResultBean killoldnew(Long userId, Long id) {

       //5种情况
       /*
        3，抢购空了
        4，抢购过了，不能重复抢
        5，获取抢购权力*/
       //获取当前秒杀商品的信息(直接从数据库中获取，最耗时，需要改善从缓存中)
        //TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(id);
        StringBuilder infoKey = new StringBuilder("miaosha:info:").append(id);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(infoKey.toString());

        //1，未开始
        if ("0".equals(product.getMiaoshaStatus())){
            throw  new MiaoShaException("当前商品未开始");
        }
        //2，已结束
        if ("2".equals(product.getMiaoshaStatus())){
            throw  new MiaoShaException("当前商品已结束");
        }
        //3.拼接用户确权的key
        StringBuilder key = new StringBuilder("miaosha:user:").append(id);
        Boolean isMember = redisTemplate.opsForSet().isMember(key.toString(), userId);
        //抢购过了，不能重复抢
        if (isMember) {
            throw new MiaoShaException("您已经抢到，请勿重复抢购...");
        }

        //加锁
        //synchronized (this),单机版则可以，this是相同的(单例)，但是集群则不同
        //集群采用共同锁---分布式锁

        //获取当前活动对应的商品信息
        StringBuilder killkey = new StringBuilder("miaosha:product:").append(id);
        Long productId = (Long) redisTemplate.opsForList().leftPop(killkey.toString());
        if (productId == null){
            throw new MiaoShaException("已经被抢购空了");
        }

        //抢购成功，添加用户信息到miaosha:user:中
        redisTemplate.opsForSet().add(key.toString(),userId);
        return new ResultBean("200","抢购成功~");
    }

    @Override
    public ResultBean kill(Long userId, Long seckillId, String path) {
        //判断redis中是否存在合法path
        StringBuilder pathKey = new StringBuilder("miaosha:").append(userId)
                .append(":").append(seckillId);
        Object o = redisTemplate.opsForValue().get(pathKey.toString());
        if (o == null){
            //path不存在，抛出异常
            throw new MiaoShaException("不是合法的秒杀路径");
        }
        //路径path一次性有效,用完立刻删除
        redisTemplate.delete(pathKey.toString());


        //5种情况
       /*
        3，抢购空了
        4，抢购过了，不能重复抢
        5，获取抢购权力*/
        //获取当前秒杀商品的信息(直接从数据库中获取，最耗时，需要改善从缓存中)
        //TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(id);
        //保存在List key=miaosha:info:seckillId ----------value=productId{101,101,101,101,101}
        StringBuilder infoKey = new StringBuilder("miaosha:info:").append(seckillId);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(infoKey.toString());

        //1，未开始
        if ("0".equals(product.getMiaoshaStatus())){
            throw  new MiaoShaException("当前商品未开始");
        }
        //2，已结束
        if ("2".equals(product.getMiaoshaStatus())){
            throw  new MiaoShaException("当前商品已结束");
        }
        //3.拼接用户确权的key
        StringBuilder key = new StringBuilder("miaosha:user:").append(seckillId);
        //4.先行添加获取到了抢购权的用户,若后面List弹出失败，则移除该用户信息--
        //   可避免一个用户抢多件(抢占了名额)的情况(这个用户上一次抢购到商品还未添加到redis,此用户有抢到了商品)
        //   虽然用户信息不能重复添加到Set中，但是导致10件商品只卖出9件(只有9个用户信息保存在set中)
        Long result = redisTemplate.opsForSet().add(key.toString(), userId);
        //result==0,则表示此用户在set中已存在，所以添加set进不去
        if (result == 0){
            throw new MiaoShaException("您已经抢到，请勿重复抢购...");
        }


        //获取当前活动对应的商品信息
        StringBuilder killkey = new StringBuilder("miaosha:product:").append(seckillId);
        Long productId = (Long) redisTemplate.opsForList().leftPop(killkey.toString());
        if (productId == null){
            //将之前存入set的用户移除
            redisTemplate.opsForSet().remove(key.toString(),userId);
            throw new MiaoShaException("已经被抢购空了");
        }

        //抢购成功
        // TODO 生成订单信息
        // 需要字段 user_id,product_id,count(1),product_price,orderNo
        //1.生成唯一的订单号orderNo
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmssSSSS");
        StringBuilder orderNo = new StringBuilder(format.format(System.currentTimeMillis())).append(userId);
        //2.发送给订单服务的消息map参数
        Map<String,Object> params = new HashMap<>();
        params.put("userId",userId);
        params.put("productId",product.getProductId());
        params.put("count",1);
        params.put("productPrice",product.getSalePrice());
        params.put("orderNo",orderNo.toString());
        //3.发送给Mysql减少一件秒杀商品的库存的信息
        params.put("seckillId",seckillId);
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.ORDER_EXCHANGE,"order.create",params);

        //返回订单编号
        return new ResultBean("200",orderNo);
    }

    //动态获取秒杀path
    @Override
    public ResultBean getPath(Long userId, Long seckillId) {
        //(前提也是要用户已经登录和到了秒杀时间)
        //保存 miaosha:userId:seckillId---path保存到redis
        StringBuilder infoKey = new StringBuilder("miaosha:info:").append(seckillId);
        TMiaoshaProduct product = (TMiaoshaProduct) redisTemplate.opsForValue().get(infoKey.toString());

        //1，未开始
        if ("0".equals(product.getMiaoshaStatus())){
            throw  new MiaoShaException("当前商品未开始");
        }
        //2，已结束
        if ("2".equals(product.getMiaoshaStatus())){
            throw  new MiaoShaException("当前商品已结束");
        }

        //生成动态path
        String path = UUID.randomUUID().toString();
        //保存到redis(设置时间)
        StringBuilder pathKey = new StringBuilder("miaosha:").append(userId)
                .append(":").append(seckillId);
        redisTemplate.opsForValue().set(pathKey.toString(),path);
        redisTemplate.expire(pathKey.toString(),1, TimeUnit.MINUTES);
        return new ResultBean("200",path);
    }
}
