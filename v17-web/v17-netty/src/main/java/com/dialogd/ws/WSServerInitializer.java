package com.dialogd.ws;

import com.dialogd.netty.HelloHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @Author: DJA
 * @Date: 2019/11/26
 */
public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * @param socketChannel   在channel注册后会执行该初始化方法
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //1.通过SocketChannel获取对应的管道对象
        ChannelPipeline pipeline = socketChannel.pipeline();
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


        //3.继续给管道添加自定义Handler
        pipeline.addLast(new WSHandler());

    }
}
