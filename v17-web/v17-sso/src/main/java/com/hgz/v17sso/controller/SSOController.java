package com.hgz.v17sso.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.constant.MQConstant;
import com.hgz.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: DJA
 * @Date: 2019/11/13
 */
@Controller
@RequestMapping("sso")
public class SSOController {

    @Reference(check = false)
    private IUserService userService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

   //异步，适合异步调用或APP交互
    @PostMapping("checkLogin")
    @ResponseBody
    public ResultBean checkLogin(TUser user,HttpServletResponse response){
        ResultBean resultBean = userService.checkLogin(user);
        if ("200".equals(resultBean.getStatusCode())){
            //1.获取到uuid
            String uuid = (String) resultBean.getData();
            //2.设置回话级Cookie
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setDomain("qf.com");
            //3.响应给客户端
            response.addCookie(cookie);
        }
        return resultBean;
    }

    //同步当时，跳转到PC页面
    @PostMapping("checkLogin4PC")
    public String checkLogin4PC(TUser user, HttpServletResponse response,
                                HttpServletRequest request,
                                @CookieValue(name = "cart_token",required = false) String cartToken){
        //1.用户服务做认证，判断当前用户账户信息是否正确
        ResultBean resultBean = userService.checkLogin(user);
        //2.如果正确，则在服务器保存凭证
        if ("200".equals(resultBean.getStatusCode())){
            //TODO 设置cookie给客户端，保存凭证
            //1.获取到登录凭证uuid(jwtToken)
            Map<String,String> tokens = (Map<String, String>) resultBean.getData();
            String uuid = tokens.get("jwtToken");
            //2.设置会话级Cookie
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            //设置cookie的域名为父域名，解决cookie跨域的问题qf.com  (如果不设置父域名，sso.qf.com的user_token的cookie不会再子域名cart.qf.com中获取到)
            cookie.setDomain("qf.com");
            cookie.setHttpOnly(true);
            //3.响应给客户端
            response.addCookie(cookie);

            //发送消息到交换机(告知已经转为登录状态)，用户购物车系统或者其他系统
            //需要两个参数，redis中未登录cartToken和已登录用户标识username
            Map<String,String> params = new HashMap<>();
            params.put("no_login_key",cartToken);
            params.put("login_key",tokens.get("username"));
            rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.SSO_EXCHANGE,"user.login",params);

            //正确则跳转到首页(Referer实现从哪里点击登录，成功后返回那个页面)
            return "redirect:http://localhost:9091";
            //String referer = request.getParameter("referer");
            //return "redirect:"+referer;
        }
        //3.如果不正确，则返回登录页面
        return "index";
    }

    //注销
    @GetMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name = "user_token",required = false) String uuid,
                             HttpServletResponse response){
        if (uuid != null){
            Cookie cookie = new Cookie("user_token",uuid);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setDomain("qf.com");
            //将cookie注销，设置有效时间为0
            cookie.setMaxAge(0);
            //3.响应给客户端
            response.addCookie(cookie);
            return new ResultBean("200","ok");
        }
        return new ResultBean("404","fail");
    }

    @GetMapping("logout4PC")
    public String logout4PC(){
        return null;
    }

    @GetMapping("checkIsLogin")    //注解@CookieValue
    @CrossOrigin(origins = "*",allowCredentials = "true")   //跨域问题：基于同源策略（协议，域名，端口）
    @ResponseBody
    public ResultBean checkIsLogin(@CookieValue(name = "user_token",required = false) String uuid){
        //TODO 获取到cookie
        //1.从cookie中获取到uuid唯一凭证
        if (uuid != null){
            //2.交给service,从redis中根据key获取到value
            return userService.checkIsLogin(uuid);
        }
        return new ResultBean("404",null);
    }

    @GetMapping("checkIsLogin4PC")
    public String checkIsLogin4PC(){
        return null;
    }

    //Ajax调用会遇到跨域问题
    @RequestMapping("checkIsLoginJsonp")
    @ResponseBody
    public String checkIsLoginJsonp(@CookieValue(name = "user_token",required = false) String uuid,
                                    String callback) throws JsonProcessingException {

        ResultBean resultBean = null;
        if(uuid != null){
            resultBean = userService.checkIsLogin(uuid);
        }else{
            //3.返回找不到的结果
            resultBean = new ResultBean("404", null);
        }
        //将对象转换为json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultBean);
        //回调客户端的方法
        return callback+"("+json+")";//deal(json) padding
    }

    //判断是否已经登录
   /* @GetMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        //TODO 获取到cookie
        //1.从cookie中获取到uuid唯一凭证
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if ("user_token".equals(cookie.getName())){
                    String uuid = cookie.getValue();
                    //2.交给service,从redis中根据key获取到value
                    ResultBean resultBean = userService.checkIsLogin(uuid);
                    return resultBean;
                }
            }
        }

//        String username = (String) request.getSession().getAttribute("user");
//        if (username != null){
//            return new ResultBean("200",username);
//        }
        return new ResultBean("404",null);
    }*/
}
