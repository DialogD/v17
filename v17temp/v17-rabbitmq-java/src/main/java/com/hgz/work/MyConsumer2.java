package com.hgz.work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: DJA
 * @Date: 2019/11/5
 * 消费者，从消息队列中获取数据
 */
public class MyConsumer2 {
    private static final String QUEUE_NAME = "work-queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建连接，连接上MQ服务器
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("114.55.39.2");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/java1907");
        connectionFactory.setUsername("java1907");
        connectionFactory.setPassword("123");

        Connection connection = connectionFactory.newConnection();
        //2.基于这个连接对象，创建channel管道
        Channel channel = connection.createChannel();

        //限流--每次只能放行一个消息
        channel.basicQos(1);

        //3.创建一个消费者对象,并写好消息逻辑
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
              
                System.out.println("消费者2：接收到的消息是："+message);
                //手动确认模式，告知MQ服务器，消息已经被处理
                //multiple:表示是否批量确认
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        //4.需要让消费者监听队列
        //autoAck:true:自动确认模式
        // autoAck=false:手动确认模式
        channel.basicConsume(QUEUE_NAME,false,consumer);
    }
}
