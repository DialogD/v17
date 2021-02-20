package com.hgz.v17item.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/5
 */
@Configuration
public class CommonConfig {

    @Bean  //交给spring管理  @Bean只执行一次
    public ThreadPoolExecutor initThreadPoolExecutor(){
        //获取当前服务器的CPU核数
        int processors = Runtime.getRuntime().availableProcessors();
        //结论：自己创建线程池   ThreadPoolExecutor对象来创建
        ThreadPoolExecutor pool = new ThreadPoolExecutor(processors, processors * 2, 1L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100));
        return pool;


    }


}
