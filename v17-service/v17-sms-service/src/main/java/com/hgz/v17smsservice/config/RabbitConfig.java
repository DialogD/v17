package com.hgz.v17smsservice.config;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 * 配置交换机和队列的绑定
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initTopicSmsExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.SMS_EXCHANGE,true,false);
        return topicExchange;
    }

    @Bean
    public Queue initSmsCodeQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.SMS_CODE_QUEUE,true,false,false);
        return queue;
    }

    @Bean
    public Binding bindSmsCodeExchange(Queue initSmsCodeQueue,
                                             TopicExchange initTopicSmsExchange){
        return BindingBuilder.bind(initSmsCodeQueue).to(initTopicSmsExchange).with("sms.code");
    }
}
