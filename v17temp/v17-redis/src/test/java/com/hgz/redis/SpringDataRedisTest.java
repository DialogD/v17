package com.hgz.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.xml.transform.Source;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void stringTest(){
        //序列化
        redisTemplate.opsForValue().set("smallTarget","五百万");
        Object o = redisTemplate.opsForValue().get("smallTarget");
        System.out.println(o);
    }

    @Test
    public void hashTest(){
        redisTemplate.opsForHash().put("book","name","c+++++");
        Object o = redisTemplate.opsForHash().get("book", "name");
        System.out.println(o);
    }

    @Test
    public void numberTest(){
        //value默认也是jdk方式的序列化--不支持数字运算
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set("money","1000");
        redisTemplate.opsForValue().increment("money",1000);
        Object money = redisTemplate.opsForValue().get("money");
        System.out.println(money);
    }

    @Test
    public void otherTest(){
        redisTemplate.opsForValue().increment("money",-500);
    }

    @Test
    public void ttlTest(){
        redisTemplate.opsForValue().set("18807085450","666");
        redisTemplate.expire("18807085450",2, TimeUnit.SECONDS);
        Long expire = redisTemplate.getExpire("18807085450");
        System.out.println(expire);
    }
}
