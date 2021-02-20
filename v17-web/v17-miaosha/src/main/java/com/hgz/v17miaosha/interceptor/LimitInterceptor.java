package com.hgz.v17miaosha.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/22
 */
@Component
@Slf4j
public class LimitInterceptor implements HandlerInterceptor {


    //1秒产生2个令牌（默认设置为2）
    //private RateLimiter rateLimiter = RateLimiter.create(2);
    //交给IOC管理  @Bean
    @Autowired
    private RateLimiter rateLimiter;

    /*@RabbitListener(queues = "limit-queue")
    @RabbitHandler
    public void processRateLimit(int rate){
        System.out.println("MQ执行了修改令牌数...");
        rateLimiter.setRate(rate);
    }*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("目前拦截器限流令牌数rate={}",rateLimiter.getRate());
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MICROSECONDS);
        if (!tryAcquire){
            log.info("获取令牌失败，限流开启...");
            return false;
        }
        log.info("获取令牌成功，正常业务逻辑处理...");
        return true;
    }
}
