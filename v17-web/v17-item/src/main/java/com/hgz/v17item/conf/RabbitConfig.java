package com.hgz.v17item.conf;

import com.hgz.commons.constant.MQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author: DJA
 * @Date: 2019/11/10
 * Item-rabbitmq
 */
@Configuration
public class RabbitConfig {

    //1.声明队列
    @Bean
    public Queue initProductItemQueue(){
        Queue queue = new Queue(MQConstant.QUEUE.CENTER_PRODUCT_EXCHANGE_ITEM_QUEUE,true,false,false);
        return queue;
    }

    //2.绑定交换机
    @Bean
    public TopicExchange initTopicExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,true,false);
        return topicExchange;
    }

    @Bean
    public Binding bindProductItemExchange(Queue initProductItemQueue,
                                           TopicExchange initTopicExchange){
        return BindingBuilder.bind(initProductItemQueue).to(initTopicExchange).with("product.add");
    }
}
