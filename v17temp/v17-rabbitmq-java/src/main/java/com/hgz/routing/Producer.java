package com.hgz.routing;


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
    private static final String EXCHANGE_NAME = "direct_exchange";

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

        //3.声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        //4.发送消息
        String message = "重大的足球消息...";
        channel.basicPublish(EXCHANGE_NAME,"football",null,message.getBytes());

        String message2 = "重大的篮球消息...";
        channel.basicPublish(EXCHANGE_NAME,"basketball",null,message2.getBytes());

        System.out.println("发送消息成功~");
    }

}
