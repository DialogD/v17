package com.hgz.v17miaosha.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/22
 */
@Component
@RequestMapping("limit")
@Slf4j
public class LimiterController {

    private RateLimiter rateLimiter = RateLimiter.create(2);

    @RequestMapping("miaosha")
    @ResponseBody
    public String miaosha(){
       /* //1.获取令牌
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MICROSECONDS);
        if (!tryAcquire){
            System.out.println("获取令牌失败，正在排队中...");
            return "获取令牌失败，正在排队中...";
        }
        //2.获取令牌成功，正常执行秒杀逻辑
        log.info("获取令牌成功...");*/
        return "Success,执行秒杀逻辑...";
    }

    public static void main(String[] args){
        RateLimiter rateLimiter = RateLimiter.create(10.0);
        System.out.println(rateLimiter.acquire(20));
        System.out.println(rateLimiter.acquire(1));
        System.out.println(rateLimiter.acquire(1));
    }
}
