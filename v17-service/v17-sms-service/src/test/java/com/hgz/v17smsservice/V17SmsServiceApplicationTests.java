package com.hgz.v17smsservice;

import com.hgz.api.ISMSService;
import com.hgz.api.pojo.SMSResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SmsServiceApplicationTests {

    @Autowired
    private ISMSService ismsService;

    @Test
    public void sendMessageTest() {
        SMSResponse smsResponse = ismsService.sendMessage("18807085450", "888888");
        System.out.println(smsResponse);
    }

}
