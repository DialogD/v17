package com.hgz.v17center.config;

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
    public TopicExchange initTopicExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,true,false);
        return topicExchange;
    }
}
