package com.hgz.v17msg.ws;

import com.hgz.v17msg.pojo.Message;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 */
@Component
@ChannelHandler.Sharable //设置handler在channel中共享
@Slf4j
public class HeartHandler extends SimpleChannelInboundHandler<Message> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {

        if ("2".equals(msg.getMsgType())){
            //是需要处理的心跳包
            log.info("{},发来一个心跳包请求",ctx.channel().remoteAddress());
            return;
        }

        //如果不是心跳包，则还是交给下一个Handler处理
        ctx.fireChannelRead(msg);

    }
}
