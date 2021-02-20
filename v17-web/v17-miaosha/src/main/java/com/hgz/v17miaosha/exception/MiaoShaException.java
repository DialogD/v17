package com.hgz.v17miaosha.exception;

/**
 * @Author: DJA
 * @Date: 2019/11/21
 * 一般自定义异常，都会继承于RuntimeException
 */
public class MiaoShaException extends RuntimeException {

    public MiaoShaException(String message){
        super(message);
    }
}
