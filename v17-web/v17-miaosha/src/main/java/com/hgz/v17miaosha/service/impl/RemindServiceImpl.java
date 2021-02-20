package com.hgz.v17miaosha.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IRemindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 */
@Service
public class RemindServiceImpl implements IRemindService {

    @Autowired
    private TMiaoshaProductMapper miaoshaProductMapper;

    @Resource(name = "myStringRedisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public ResultBean add(Integer txseckillId, Integer userId) {
        //redis方案
        // zSet --- score时间戳
        //key   ---------------- value ---------------------score分数-时间戳
        //miaosha:remind         map(userId,txseckillId)      201911271809

        //1.获取当前秒杀活动的开启时间
        TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(Long.parseLong(txseckillId.toString()));
        //2.提前10分钟提醒用户
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(product.getStartTime());
        //计算秒杀开始时间-10分钟
        calendar.add(Calendar.MINUTE,-10);
        Date remindTime = calendar.getTime();

        //转时间戳
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String score = format.format(remindTime);

        Map<String,Integer> map = new HashMap<>();
        map.put("txseckillId",txseckillId);
        map.put("userId",userId);
        ObjectMapper objectMapper = new ObjectMapper();
        //map --> json
        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //将时间信息保存在zSet中
        redisTemplate.opsForZSet().add("miaosha:remind",json,Double.valueOf(score));
        return new ResultBean("200","已保存提醒设置信息");
    }
}
