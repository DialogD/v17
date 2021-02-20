package com.hgz.v17miaosha.config;

import com.hgz.v17miaosha.interceptor.LimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: DJA
 * @Date: 2019/11/22
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LimitInterceptor limitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(limitInterceptor).addPathPatterns("/**");
    }
}
