package com.hgz.v17userservice.config;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DJA
 * @Date: 2019/11/7
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initTopicSmsExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.SMS_EXCHANGE,true,false);
        return topicExchange;
    }

    @Bean
    public TopicExchange initTopicEmailExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.EMAIL_EXCHANGE,true,false);
        return topicExchange;
    }
}
