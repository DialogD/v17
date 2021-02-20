package com.dialogd.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author: DJA
 * @Date: 2019/11/26
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {
        //1.定义主线程组，处理客户端的请求--默认是CPU核数的两倍
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //2.定义从线程组，处理主线程交过来的任务，完成IO操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //3.创建Netty服务启动对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                ////5.设置NIO的双向通道
                .channel(NioServerSocketChannel.class)
                //TODO 具体如何处理客户端的请求 --执行该初始化方法
                .childHandler(new ServerInitializer());

        //异步绑定操作---》syn同步
        serverBootstrap.bind(8888).sync();

        System.out.println("Netty服务开启，端口为8888");
    }
}
