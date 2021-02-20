package com.hgz.v17msg.handler;

import com.hgz.v17msg.utils.ChannelUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/28
 */
@Slf4j
public class MyReadTimeoutHandler extends ReadTimeoutHandler {


    public MyReadTimeoutHandler(int timeoutSeconds) {
        super(timeoutSeconds);
    }

    public MyReadTimeoutHandler(long timeout, TimeUnit unit) {
        super(timeout, unit);
    }

    @Override
    protected void readTimedOut(ChannelHandlerContext ctx) throws Exception {
        super.readTimedOut(ctx);
        //添加业务逻辑
        log.info("{}断开连接",ctx.channel().remoteAddress());
        //从我们管理的映射关系移除此连接
        ChannelUtils.remove(ctx.channel());
    }
}
