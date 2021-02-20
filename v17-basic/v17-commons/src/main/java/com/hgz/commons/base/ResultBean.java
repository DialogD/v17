package com.hgz.commons.base;

import java.io.Serializable;

/**
 * @Author: DJA
 * @Date: 2019/10/29 22:06
 */

public class ResultBean<T> implements Serializable {

    private String statusCode;   //返回状态码

    private T data;    //返回数据

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResultBean(String statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResultBean() {
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "statusCode='" + statusCode + '\'' +
                ", data=" + data +
                '}';
    }
}
