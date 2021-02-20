package com.hgz.v17order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: DJA
 * @Date: 2019/11/19
 */
@Component
@ConfigurationProperties(prefix = "alipay")
@Data
public class AlipayClientProperties {

    private String serverUrl;
    private String appId;
    private String privateKey;
    private String format;
    private String charset;
    private String alipayPublicKey;
    private String signType;
}
