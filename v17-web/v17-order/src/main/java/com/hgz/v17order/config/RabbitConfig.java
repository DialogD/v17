package com.hgz.v17order.config;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DJA
 * @Date: 2019/11/21
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initOrderTopicExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.ORDER_EXCHANGE,true,false);
        return topicExchange;
    }

    @Bean
    public Queue initOrderQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.ORDER_QUEUE,true,false,false);
        return queue;
    }

    @Bean
    public Binding bindOrderExchange(Queue initOrderQueue,
                                     TopicExchange initOrderTopicExchange){
        return BindingBuilder.bind(initOrderQueue).to(initOrderTopicExchange).with("order.create");
    }
}
