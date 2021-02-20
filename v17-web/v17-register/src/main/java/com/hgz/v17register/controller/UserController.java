package com.hgz.v17register.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: DJA
 * @Date: 2019/11/11
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Reference(check = false)
    private IUserService userService;

    //用户名唯一性验证
    @GetMapping("checkUserNameIsExists/{username}")
    @ResponseBody
    public ResultBean checkUserNameIsExists(@PathVariable("username") String username){
        return userService.checkUserNameIsExists(username);
    }

    //手机号唯一性验证
    @GetMapping("checkPhoneIsExists/{phone}")
    @ResponseBody
    public ResultBean checkPhoneIsExists(@PathVariable("phone") String phone){
        return userService.checkPhoneIsExists(phone);
    }

    //邮箱唯一性验证
    @GetMapping("checkEmailIsExists/{email}")
    @ResponseBody
    public ResultBean checkEmailIsExists(@PathVariable("email") String email){
        return userService.checkEmailIsExists(email);
    }

    @PostMapping("generateCode/{identification}")
    @ResponseBody
    public ResultBean generateCode(@PathVariable("identification") String identification){
        return userService.generateCode(identification);
    }

    /**
     * 适合处理异步请求
     * @param user
     * @return
     */
    @PostMapping("register")
    @ResponseBody
    public ResultBean register(TUser user){
        //需要调用重写insert()方法
        //String encode1 = passwordEncoder.encode("123456");  //加密
        return null;
    }

    /**
     * 适合处理同步请求
     * @param user
     * @return
     */
    @PostMapping("register4PC")
    @ResponseBody
    public String register4PC(TUser user){
        return null;
    }

    //邮箱激活
    @GetMapping("activating")
    public String activating(String token){
        return null;
    }

}
