package com.hgz.v17msg.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgz.v17msg.pojo.Message;
import com.hgz.v17msg.utils.ChannelUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: DJA
 * @Date: 2019/11/26
 *
 */
@Component
@ChannelHandler.Sharable //设置handler在channel中共享
@Slf4j
public class WSHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //1.获取客户端传递过来的消息
        String text = msg.text();
        //2.将msg转换为对象
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(text, Message.class);
        //3.判断请求类型
        if ("1".equals(message.getMsgType())){
            //连接请求
            String userId = (String) message.getData();
            log.info("地址{},{}与服务器建立了长连接",ctx.channel().remoteAddress(),userId);

            //把当前通道与映射关系保存起来
            ChannelUtils.add(userId,ctx.channel());
            log.info("{}与{}建立了映射关系",userId,ctx.channel());
        }

        //面向对象编程OOP-----OCP开闭原则
        //对修改关闭，可扩展新增

        //交给下一个处理器处理
        ctx.fireChannelRead(message);

        //通过责任链的编程模式来改变多个if/else写法
    }

}
