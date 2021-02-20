package com.dialogd.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author: DJA
 * @Date: 2019/11/26
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * @param socketChannel   在channel注册后会执行该初始化方法
     * @throws Exception
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //1.通过SocketChannel获取对应的管道对象
        ChannelPipeline pipeline = socketChannel.pipeline();
        //2.给管道添加Handler
        //HttpServerCodec : 做编解码
        //当请求--》服务端，服务端需要做解码，响应信息--》客户端需要做编码
        //netty接收的是二进制数据
        pipeline.addLast(new HttpServerCodec());
        //3.继续给管道添加自定义Handler
        pipeline.addLast(new HelloHandler());

    }
}
