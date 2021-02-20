package com.hgz.v17miaosha.handler;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: DJA
 * @Date: 2019/11/25
 */
@Component
public class LimiterHandler {

    @Autowired
    private RateLimiter rateLimiter;

    @RabbitListener(queues = "limit-queue")
    @RabbitHandler
    public void processRateLimit(double rate){
        System.out.println("MQ执行了修改令牌数...");
        rateLimiter.setRate(rate);
    }
}
