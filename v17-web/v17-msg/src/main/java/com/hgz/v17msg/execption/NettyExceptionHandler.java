package com.hgz.v17msg.execption;

import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 */
@ControllerAdvice
@Slf4j
public class NettyExceptionHandler {
    /**
     * ReadTimeoutException超时之后没有抛出异常，只是输出异常信息
     * @param e
     */
    @ExceptionHandler(ReadTimeoutException.class)
    public void readTimeoutHandler(Exception e){
        log.info("timeOut...exception");
    }

}
