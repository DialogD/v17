package com.hgz.v17springbootjavamail.controller;

import com.hgz.v17springbootjavamail.entity.User;
import com.hgz.v17springbootjavamail.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;


/**
 * @Author: DJA
 * @Date: 2019/11/10
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private IMailService mailService;

    @RequestMapping("register")
    public String register(String username, String password, String email){
        User user = new User(101, username,password,email);
        System.out.println(user.toString());
        mailService.sendEmailToUser(user);   //发送邮件

        return "ok";
    }
}
