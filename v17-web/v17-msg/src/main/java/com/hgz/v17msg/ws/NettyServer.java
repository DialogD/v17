package com.hgz.v17msg.ws;

import com.hgz.v17msg.handler.MyReadTimeoutHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 * 只要实现了CommandLineRunner接口，在SpringBoot启动的时候，会自动执行run()
 */
@Component
@Slf4j
public class NettyServer implements CommandLineRunner {

    @Autowired
    private WSHandler wsHandler;

    @Autowired
    private HeartHandler heartHandler;

    @Value("${netty.server.port}")
    private int nettyServerPort;

    @Override
    public void run(String... args) throws Exception {
        //完成netty服务的启动

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
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        //1.通过SocketChannel获取对应的管道对象
                        ChannelPipeline pipeline = channel.pipeline();
                        //2.给管道添加Handler
                        pipeline.addLast(new HttpServerCodec());
                        //3.考虑到传输数据，设置支持大数据流
                        pipeline.addLast(new ChunkedWriteHandler());
                        //4.对HTTPMessage做聚合,设置支持传输的最大长度为1024*32 字节
                        pipeline.addLast(new HttpObjectAggregator(1024*32));
                        //设置跟http协议相关的处理器--end
                        //5,设置跟websocket相关的设置
                        //用于指定给客户端连接访问的路由 : /ws
                        //对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

                        //超时为跟服务器交互的客户端，断开长连接
                        pipeline.addLast(new MyReadTimeoutHandler(9, TimeUnit.SECONDS));

                        //3.继续给管道添加自定义Handler
                        pipeline.addLast(wsHandler);
                        pipeline.addLast(heartHandler);
                    }
                });

        //异步绑定操作---》syn同步
        serverBootstrap.bind(nettyServerPort).sync();
        log.info("端口为{}的netty服务启动完毕",nettyServerPort);

    }
}
