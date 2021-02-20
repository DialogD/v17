package com.hgz.v17miaosha.task;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/11/21
 * 定点扫描秒杀活动表，初始化数据到redis中
 */
@Component
public class MiaoShaTask {

    @Autowired
    private TMiaoshaProductMapper miaoshaProductMapper;

    @Resource(name = "longRedisTemplate")
    private RedisTemplate<String,Long> longRedisTemplate;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    //注：正常是定点扫描当前可以参数秒杀的商品
    // 这里每隔5秒扫描为了便于测试
    @Scheduled(cron = "0/5 * * * * ? ")
    public void scanCanStartMiaoSha(){
        //时间now在startTime~endTime之间，且status=0
        //查询当前可以参与秒杀的商品的集合
        List<TMiaoshaProduct> list = miaoshaProductMapper.getCanMiaoShaProduct();
        //判断是否存在集合
        if (list != null && !list.isEmpty()){
            //初始化redis中待秒杀的商品的信息
            for (TMiaoshaProduct product : list) {
                StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                //存入信息，尽量不要循环遍历写入redis 1.流水线方式   2.直接存储集合list
                //方式1：流水线方式
                /*redisTemplate.executePipelined(new SessionCallback<Object>() {
                    @Override
                    public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                        for (Integer i = 0; i < product.getCount(); i++) {
                            redisTemplate.opsForList().leftPush(key.toString(),product.getProductId());
                        }
                        return null;
                    }
                });*/
                //方式2：存入集合方式，不过redisTemplate中需要<String,Long>,否则不调用leftPushAll()
                List<Long> ids = new ArrayList<>(product.getCount());
                for (Integer i = 0; i < product.getCount(); i++) {
                    ids.add(product.getProductId());
                }
                //将集合存入redis
                longRedisTemplate.opsForList().leftPushAll(key.toString(),ids);

                //初始化完毕后需要更新status状态
                //如果不是定点的情况下，是有风险的(可能在更新状态前又初始化了)
                product.setMiaoshaStatus("1");
                miaoshaProductMapper.updateByPrimaryKey(product);

                //保存当前活动的商品信息到redis中
                StringBuilder infoKey = new StringBuilder("miaosha:info:").append(product.getId());
                redisTemplate.opsForValue().set(infoKey.toString(),product);
            }
            System.out.println("redis数据初始化完成...");
        }
    }

    //Redis定期清理当前已经结束的活动进行清理
    @Scheduled(cron = "0/5 * * * * ? ")
    public void scanCanStopMiaoSha(){
        //1.查询当前已经开启秒杀且过期时间的活动
        List<TMiaoshaProduct> list = miaoshaProductMapper.getCanStopMiaoSha();
        if (list!=null && !list.isEmpty()){
            for (TMiaoshaProduct product : list) {
                //清理Redis中的数据miaosha:info:   和miaosha:product:
                //redis会自动清除
                StringBuilder key = new StringBuilder("miaosha:product:").append(product.getId());
                redisTemplate.delete(key.toString());

                StringBuilder infoKey = new StringBuilder("miaosha:info:").append(product.getId());
                redisTemplate.delete(infoKey.toString());

                //更新状态1--》2已结束的状态
                product.setMiaoshaStatus("2");
                miaoshaProductMapper.updateByPrimaryKey(product);

                //TODO 扫描库存count是否为0，不为0则发消息将库存异步写商品服务的库存中
            }
            //slf4j--->log
            System.out.println("清理完成..");
        }

    }

}
