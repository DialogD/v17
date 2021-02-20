package com.hgz.v17springbootjavamail.service.impl;

import com.hgz.v17springbootjavamail.entity.User;
import com.hgz.v17springbootjavamail.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: DJA
 * @Date: 2019/11/10
 */
@Service
public class MailServiceImpl implements IMailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Value("${mail.fromAddr}")
    private String from;

    @Override
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendHTMLMail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            mailSender.send(message);
            System.out.println("发送html成功~");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendAttachmentMail(String to, String subject, String content, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);

            FileSystemResource fileSystemResource = new FileSystemResource(filePath);
            String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
            helper.addAttachment(fileName,fileSystemResource);

            mailSender.send(message);
            System.out.println("发送加附件成功~");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendTemplateMail() {
       //设置变量值
        Context context = new Context();
        context.setVariable("name","张老三");
        //将变量存入模板
        String content = templateEngine.process("templateEmail", context);
        //发送--通过html方式
        sendHTMLMail("1228516160@qq.com","工作汇报情况",content);
        System.out.println("template模板发送成功~");
    }

    @Override
    public String sendEmailToUser(User user) {
        //设置变量值
        Context context = new Context();
        context.setVariable("name",user.getUsername());

        String uuid = UUID.randomUUID().toString();
        System.out.println("uuid="+uuid);
        String url = "http://localhost:9988/activate/"+user.getId()+"/"+uuid;
        context.setVariable("url",url);
        //将变量存入模板
        String content = templateEngine.process("templateEmail", context);
        //发送--通过html方式
        sendHTMLMail(user.getEmail(),"邮件激活测试",content);
        //存入redis  uuid-userId   有效期2分钟
        redisTemplate.opsForValue().set(uuid,user.getId(),2,TimeUnit.MINUTES);
        System.out.println("template模板发送成功~");
        return uuid;
    }


}
