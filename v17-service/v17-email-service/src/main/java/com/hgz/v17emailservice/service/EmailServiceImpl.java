package com.hgz.v17emailservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IEmailService;
import com.hgz.commons.base.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @Author: DJA
 * @Date: 2019/11/12
 */
@Service
public class EmailServiceImpl implements IEmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${mail.fromAddr}")
    private String from;


    @Override
    public ResultBean sendBirthday(String to, String username) {
        //1.构建邮件对象
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject("【dialogd】生日祝福");
            //邮件的内容由模板来产生
            Context context = new Context();
            context.setVariable("username",username);
            String content = templateEngine.process("birthday", context);
            helper.setText(content,true);

            //2.发送邮件
            javaMailSender.send(message);
            //3.反馈成功
            return new ResultBean("200","ok");
        } catch (MessagingException e) {
            e.printStackTrace();
            //异常处理机制
        }
        return new ResultBean("500","邮件发送失败");
    }

    //邮箱激活邮件的发送
    @Override
    public ResultBean sendActivate(String to, String username) {
        return null;
    }
}
