package com.hgz.v17springbootjavamail;

import com.hgz.v17springbootjavamail.service.IMailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SpringbootJavamailApplicationTests {
    @Autowired
    private IMailService mailService;

    @Test
    public void sendSimpleMailTest() {
        mailService.sendSimpleMail("d1228516160@163.com","普通文本发送测试","hello,usual Email");
        System.out.println("发送成功~");
    }

    @Test
    public void sendHTMLMailTest(){
        mailService.sendHTMLMail("1228516160@qq.com","HTML发送测试","hello,<font color='red'>HTMLtext</font>");
    }

    @Test
    public void sendAttachmentMailTest(){
        mailService.sendAttachmentMail("d1228516160@163.com","添加附件测试","hello,附件测试","F:\\MavenIdea\\v17\\v17temp\\v17-springboot-javamail\\017832.jpg");
    }

    @Test
    public void sendTemplateMailTest(){
        mailService.sendTemplateMail();
    }

}
