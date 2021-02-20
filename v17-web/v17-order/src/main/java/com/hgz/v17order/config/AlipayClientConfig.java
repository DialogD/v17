package com.hgz.v17order.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: DJA
 * @Date: 2019/11/19
 */
@Configuration
public class AlipayClientConfig {

    @Autowired
    private AlipayClientProperties alipayClientProperties;

    @Bean
    public AlipayClient getAlipayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayClientProperties.getServerUrl(),
                alipayClientProperties.getAppId(),
                alipayClientProperties.getPrivateKey(),
                alipayClientProperties.getFormat(),
                alipayClientProperties.getCharset(),
                alipayClientProperties.getAlipayPublicKey(),
                alipayClientProperties.getSignType()); //获得初始化的AlipayClient
        return alipayClient;
    }
}
