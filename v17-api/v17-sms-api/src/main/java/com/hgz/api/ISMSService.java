package com.hgz.api;

import com.hgz.api.pojo.SMSResponse;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 * 短信服务的接口
 */
public interface ISMSService {

    public SMSResponse sendMessage(String phone, String code);

    public SMSResponse sendBirthdayGreetingMessage(String identification, String username);
}
