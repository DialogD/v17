package com.hgz.v17emailservice.config;

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
    public TopicExchange initTopicEmailExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.EMAIL_EXCHANGE,true,false);
        return topicExchange;
    }

    @Bean
    public Queue initEmailBirthdayQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.EMAIL_BIRTHDAY_QUEUE,true,false,false);
        return queue;
    }

    @Bean
    public Binding bindEmailBirthdayExchange(Queue initEmailBirthdayQueue,
                                             TopicExchange initTopicEmailExchange){
        return BindingBuilder.bind(initEmailBirthdayQueue).to(initTopicEmailExchange).with("email.birthday");
    }
}
