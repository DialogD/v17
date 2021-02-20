package com.hgz.v17sso.config;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DJA
 * @Date: 2019/11/17
 * 单点登录的交换机
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initTopicSsoExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.SSO_EXCHANGE,true,false);
        return topicExchange;
    }
}
