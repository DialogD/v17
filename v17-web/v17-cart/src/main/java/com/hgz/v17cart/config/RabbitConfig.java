package com.hgz.v17cart.config;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DJA
 * @Date: 2019/11/17
 * 购物车服务队列和绑定交换机
 */
@Configuration
public class RabbitConfig {

    @Bean
    public TopicExchange initTopicSsoExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.SSO_EXCHANGE,true,false);
        return topicExchange;
    }

    @Bean
    public Queue initSsoCartQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.SSO_QUEUE_CART,true,false,false);
        return queue;
    }

    @Bean
    public Binding bindSsoCartExchange(Queue initSsoCartQueue,
                                       TopicExchange initTopicSsoExchange){
        return BindingBuilder.bind(initSsoCartQueue).to(initTopicSsoExchange).with("user.login");
    }
}
