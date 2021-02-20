package com.hgz.v17smsservice.handler;

import com.hgz.api.ISMSService;
import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 */
@Component
public class SMSHandler {

    @Autowired
    private ISMSService smsService;

    //处理发送验证码验证
    @RabbitListener(queues = MQConstant.QUEUE.SMS_CODE_QUEUE)
    @RabbitHandler
    public void processSMSCode(Map<String,String> params){
        String identification = params.get("identification");
        String code = params.get("code");
        smsService.sendMessage(identification,code);
    }

    //处理发生日祝福
//    @RabbitListener(queues = "")
//    @RabbitHandler
    public void processSendBrithdayGreeting(Map<String,String> params){
        String identification = params.get("identification");
        String username = params.get("username");
        smsService.sendBirthdayGreetingMessage(identification,username);
    }
}
