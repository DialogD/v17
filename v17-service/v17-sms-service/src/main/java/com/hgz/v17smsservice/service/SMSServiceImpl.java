package com.hgz.v17smsservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.hgz.api.ISMSService;
import com.hgz.api.pojo.SMSResponse;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 */
@Service
public class SMSServiceImpl implements ISMSService {

    @Override
    public SMSResponse sendMessage(String phone, String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4Feuih79MrZa4GETqbDG", "wWYC8tXEQIJJzJyQ8wcdxco5LOl0W2");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "dialogd");
        request.putQueryParameter("TemplateCode", "SMS_177247589");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
//            System.out.println(response.getData());
            //将返回结果的json转化为对象返回
            return new Gson().fromJson(response.getData(),SMSResponse.class);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    //生日祝福的短信
    @Override
    public SMSResponse sendBirthdayGreetingMessage(String identification, String username) {
        //亲爱的${username},今天是您的生日，网站给您送上9折优惠券，赶紧购买吧！
        return null;
    }
}
