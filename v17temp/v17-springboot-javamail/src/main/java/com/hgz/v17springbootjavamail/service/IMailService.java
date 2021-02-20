package com.hgz.v17springbootjavamail.service;

import com.hgz.v17springbootjavamail.entity.User;

/**
 * @Author: DJA
 * @Date: 2019/11/10
 */
public interface IMailService {
    /**
     * 发动普通文本邮件
     * @param to   发给谁
     * @param subject   标题
     * @param content   内容
     */
    void sendSimpleMail(String to,String subject,String content);

    void sendHTMLMail(String to,String subject,String content);

    /**
     *
     * @param to
     * @param subject
     * @param content
     * @param filePath  文件路径
     */
    void sendAttachmentMail(String to,String subject,String content,String filePath);

    void sendTemplateMail();

    String sendEmailToUser(User user);

}
