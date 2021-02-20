package com.hgz.v17emailservice.handler;

import com.hgz.api.IEmailService;
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
public class EmailHandler {

    @Autowired
    private IEmailService emailService;

    //处理生日祝福邮件
    @RabbitListener(queues = MQConstant.QUEUE.EMAIL_BIRTHDAY_QUEUE)
    @RabbitHandler
    public void processSendBirthday(Map<String,String> params){
        String to = params.get("to");
        String username = params.get("username");
        emailService.sendBirthday(to,username);
    }

    //处理邮件激活
    public void processSendActivate(Map<String,String> params){
        String to = params.get("to");
        String username = params.get("username");
        emailService.sendActivate(to,username);
    }


}
