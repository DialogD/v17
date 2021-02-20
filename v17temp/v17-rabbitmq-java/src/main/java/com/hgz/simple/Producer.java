package com.hgz.simple;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author: DJA
 * @Date: 2019/11/5
 * 生产者：负责发送消息给队列
 */
public class Producer {
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

        //3.声明队列
        //如果该队列不存在，则创建该队列，否则，不能创建
        //durable: 队列是否持久化（重启服务器后存在）
        //exclusive: 队列是否独占（针对这个连接）
        channel.queueDeclare("simple-queue",true,false,false,null);

        //4.发送消息
        String msg = "RabbitMQ是一个很重要的中间组件";
        channel.basicPublish("","simple-queue",null,msg.getBytes());

        System.out.println("发送消息成功~");
    }

}
