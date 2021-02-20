package com.hgz.v17springbootjavamail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: DJA
 * @Date: 2019/11/11
 */
@Controller
public class httpController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @RequestMapping("activate/{id}/{uuid}")
    public String activate(@PathVariable("id") int id,@PathVariable("uuid") String uuid){
        System.out.println("执行了httpController方法");
        System.out.println("userId="+id+",uuid="+uuid);
        int userId = (int) redisTemplate.opsForValue().get(uuid);
        if (userId==id){
            System.out.println("用户id="+id+",邮箱激活成功");
        }
        return "success";
    }

}
