package com.hgz.v17cart.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ICartService;
import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/16
 */
@Component
public class SsoHandler {

    @Reference
    private ICartService cartService;

    @RabbitListener(queues = MQConstant.QUEUE.SSO_QUEUE_CART)
    @RabbitHandler
    public void process(Map<String,String> params){
        String no_login_key = params.get("no_login_key");
        String login_key = params.get("login_key");
        //调用方法合并购物车
        cartService.merge(no_login_key,login_key);
    }
}
