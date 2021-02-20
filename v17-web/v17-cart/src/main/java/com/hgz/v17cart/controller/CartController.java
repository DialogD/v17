package com.hgz.v17cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ICartService;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @Author: DJA
 * @Date: 2019/11/15
 */
@Controller
@RequestMapping("cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @GetMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean add(@PathVariable("productId") Long productId,
                          @PathVariable("count") int count,
                          @CookieValue(name = "cart_token",required = false) String cartToken,
                          HttpServletRequest request,
                          HttpServletResponse response){

        //已登录：传递用户登录的唯一标识
        //未登录：传递cartToken

        //方式1：HttpClient 发送http请求--/sso/checkIsLogin

        //方式2：调用UserService

        //方式3：设置拦截器,获取登录request中数据username
        String userToken = (String) request.getAttribute("user");
        if (userToken != null){
            //已登录
            //发送一个异步写消息给消息中间件--存入mysql

            return cartService.add(userToken,productId,count);
        }

        //未登录
        if (cartToken == null){
            //不存在cartToken,生成唯一标识
            cartToken = UUID.randomUUID().toString();
        }
        reflushCookie(cartToken, response);
        return cartService.add(cartToken,productId,count);
    }


    @GetMapping("delete/{productId}")
    @ResponseBody
    public ResultBean delete(@PathVariable("productId") Long productId,
                             @CookieValue(name = "cart_token",required = false) String cartToken,
                             HttpServletRequest request,
                             HttpServletResponse response){
        String userToken = (String) request.getAttribute("user");
        if (userToken != null){
            //已登录  userToken为username
            return cartService.delete(userToken,productId);
        }

        if (cartToken != null){
            ResultBean resultBean = cartService.delete(cartToken, productId);
            reflushCookie(cartToken,response);
            return resultBean;
        }
        return new ResultBean("404",false);
    }

    @GetMapping("update/{productId}/{count}")
    @ResponseBody
    public ResultBean update(@PathVariable("productId") Long productId,
                             @PathVariable("count") int count,
                             @CookieValue(name = "cart_token",required = false) String cartToken,
                             HttpServletRequest request,
                             HttpServletResponse response){
        String userToken = (String) request.getAttribute("user");
        if (userToken != null){
            //已登录
            return cartService.update(userToken,productId,count);
        }

        if (cartToken != null){
            ResultBean resultBean = cartService.update(cartToken, productId, count);
            reflushCookie(cartToken,response);
            return resultBean;
        }
        return new ResultBean("404",false);
    }

    @GetMapping("query")
    @ResponseBody
    public ResultBean query(@CookieValue(name = "cart_token",required = false) String cartToken,
                            HttpServletRequest request,
                            HttpServletResponse response){
        String userToken = (String) request.getAttribute("user");
        if (userToken != null){
            //已登录
            return cartService.query(userToken);
        }

        //未登录
        if (cartToken != null){
            ResultBean resultBean = cartService.query(cartToken);
            //重刷cookie的有效时间
            reflushCookie(cartToken,response);
            return resultBean;
        }
        return new ResultBean("404",null);
    }

    private void reflushCookie(@CookieValue(name = "cart_token", required = false) String cartToken, HttpServletResponse response) {
        //写cookie到客户端，更新有效时间
        Cookie cookie = new Cookie("cart_token",cartToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setDomain("qf.com");
        cookie.setMaxAge(15*24*60*1000);  //有效期15天
        //写好响应给客户端
        response.addCookie(cookie);
    }

}
