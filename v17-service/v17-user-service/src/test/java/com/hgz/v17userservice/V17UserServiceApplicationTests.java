package com.hgz.v17userservice;

import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17UserServiceApplicationTests {

    @Autowired
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test   //加密
    public void encodeTest(){
        //MD5加密(不可逆)    abc-->def
        //网上MD5在线解密---建立字典表（原文--密文）--加大解密难度--加盐值
        String encode1 = passwordEncoder.encode("123");
        String encode2 = passwordEncoder.encode("123");
        System.out.println(encode1);
        System.out.println(encode2);  //随机盐
        /*
        *  $2a$10$dJ./d9jZS2AX9T32MA4sW.3wuOnG/XHreqzatG6Yhs1.69S9/b9t2
           $2a$10$X9GZu6sSM/pPqpAeA4TOOOELHuHnBJUOvB4apGA57JMzeSO2hqTW6
        * */
    }

    //解密
    @Test
    public void decodeTest(){
        System.out.println(passwordEncoder.matches("123", "$2a$10$dJ./d9jZS2AX9T32MA4sW.3wuOnG/XHreqzatG6Yhs1.69S9/b9t2"));
        System.out.println(passwordEncoder.matches("123", "$2a$10$X9GZu6sSM/pPqpAeA4TOOOELHuHnBJUOvB4apGA57JMzeSO2hqTW6"));

    }

    @Test
    public void emailTest() {
        ResultBean resultBean = userService.checkUserNameIsExists("zs");
        System.out.println(resultBean);
    }

    @Test
    public void checkLogin4PCTest() {
        TUser user = new TUser();
        user.setUsername("18807085450");
        user.setPassword("123");
        ResultBean resultBean = userService.checkLogin(user);
        System.out.println(resultBean);
    }

}
