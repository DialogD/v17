package com.dialogd.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

/**
 * @Author: DJA
 * @Date: 2019/11/26
 * 编写自定义Handler类
 * SimpleChannelInboundHandler:处理请求作用---In
 */
public class HelloHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        //1.获取channel
        Channel channel = channelHandlerContext.channel();
        //2.判断是否为Http请求
        if (httpObject instanceof HttpRequest) {
            //3.打印输出客户端地址信息
            System.out.println(channel.remoteAddress());
            //4.定义给客户端的相应信息
            ByteBuf content = Unpooled.copiedBuffer("Hello,This is NettyServer", CharsetUtil.UTF_8);
            //5.构建一个响应对象
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,content);
            //6.设置响应的数据类型和长度
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());
            //7.将响应信息刷给客户端
            channelHandlerContext.writeAndFlush(response);

        }
    }
}
