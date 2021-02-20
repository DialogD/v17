package com.hgz.v17miaosha.config;

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

    //交换机声明--两队列接收order-queue and order-stock-queue
    @Bean
    public TopicExchange initOrderTopicExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.ORDER_EXCHANGE,true,false);
        return topicExchange;
    }

    //Mysql中秒杀库存信息-1： order-stock-queue声明及绑定
    @Bean
    public Queue initOrderStockQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.ORDER_STOCK_QUEUE,true,false,false);
        return queue;
    }

    @Bean
    public Binding bindOrderExchange(Queue initOrderStockQueue,
                                     TopicExchange initOrderTopicExchange){
        return BindingBuilder.bind(initOrderStockQueue).to(initOrderTopicExchange).with("order.create");
    }
}
