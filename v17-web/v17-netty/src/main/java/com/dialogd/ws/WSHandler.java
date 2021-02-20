package com.dialogd.ws;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author: DJA
 * @Date: 2019/11/26
 * 编写自定义Handler类
 * 在Netty中，frame是消息的载体，TextWebSocketFrame用于处理文本对象
 */
public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    //1.创建对象，管理所有的客户端channel
    private static ChannelGroup channels = new
            DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //1.获取客户端传递过来的消息
        String text = msg.text();
        System.out.println("收到客户端的消息为："+text);
        //2.发送消息给其他服务器(写给各个客户端)
        ctx.writeAndFlush(new TextWebSocketFrame(ctx.channel().remoteAddress()+"说："+text));
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+":已经连接上服务器");
        channels.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress()+":已经断开服务器");
        channels.remove(ctx.channel());
    }


}
