package com.hgz.work;


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

        //3.声明队列
        //如果该队列不存在，则创建该队列，否则，不能创建
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //4.发送消息
        for (int i = 0; i < 10; i++) {
            String message = "正在执行第"+(i+1)+"次";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        }

        System.out.println("发送消息成功~");
    }

}
