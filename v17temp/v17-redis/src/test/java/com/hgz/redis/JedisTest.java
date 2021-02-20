package com.hgz.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/8
 */
public class JedisTest {

    @Test
    public void stringTest(){
        Jedis jedis = new Jedis("114.55.39.2",6379);
        jedis.auth("java1907");
        jedis.set("target","gaoshiqing");
        String target = jedis.get("target");
        System.out.println(target);

        for (int i = 0; i < 10; i++) {

            jedis.incr("count");
        }
        System.out.println(jedis.get("count"));

    }

    @Test
    public void otherTest(){
        Jedis jedis = new Jedis("114.55.39.2",6379);
        jedis.auth("java1907");
        jedis.lpush("list","a","c","b");
        List<String> lrange = jedis.lrange("list", 0, -1);
        for (String s : lrange) {
            System.out.println(s);
        }

        Long sadd = jedis.sadd("set", "a", "a", "b");
        System.out.println(sadd);

        Map<String, Double> map = new HashMap<String, Double>();
        map.put("java",99.0);
        map.put("php",999.9);
        jedis.zadd("hotbook",map);
    }
}
