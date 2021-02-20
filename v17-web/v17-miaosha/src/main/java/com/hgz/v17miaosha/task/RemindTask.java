package com.hgz.v17miaosha.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 */
@Component
@Slf4j
public class RemindTask {

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Scheduled(cron = "0 58 * * * *")
    public void remind(){
        //提前10分钟，提醒用户参与秒杀
        //根绝当前时间，获取分数
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String score = format.format(new Date());

        //2.
        Set<Object> reminds = redisTemplate.opsForZSet().rangeByScore("miaosha:remind", Double.valueOf(score), Double.valueOf(score));

        //3.
        for (Object remind : reminds) {
            String json = (String) remind;
            //json-->map
            ObjectMapper objectMapper = new ObjectMapper();
            Map params = null;
            try {
                params = objectMapper.readValue(json, Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Integer txseckillId = (Integer) params.get("txseckillId");
            Integer userId = (Integer) params.get("userId");
            log.info("秒杀活动提醒信息：{}-->{}",txseckillId,userId);
            //发送消息MQ
        }

        //移除掉已经提醒了的信息
        redisTemplate.opsForZSet().removeRangeByScore("miaosha:remind",
                Double.valueOf(score), Double.valueOf(score));

    }
}
