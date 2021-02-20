package com.hgz.v17emailservice;

import com.hgz.api.IEmailService;
import com.hgz.commons.base.ResultBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17EmailServiceApplicationTests {
    @Autowired
    private IEmailService emailService;

    @Test
    public void sendBirthdayTest() {
        ResultBean resultBean = emailService.sendBirthday("1228516160@qq.com", "张老三");
        System.out.println(resultBean);
    }

}
