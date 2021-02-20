package com.hgz.v17order.interceptor;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IUserService;
import com.hgz.commons.base.ResultBean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: DJA
 * @Date: 2019/11/18
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Reference
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
       if (cookies != null){
           for (Cookie cookie : cookies) {
               if ("user_token".equals(cookie.getName())){
                   ResultBean resultBean = userService.checkIsLogin(cookie.getValue());
                   if ("200".equals(resultBean.getStatusCode())){
                       //已登录
                       request.setAttribute("user",resultBean.getData());
                       return true;
                   }
               }
           }
       }
       //未登录,不放行,转到登录页面
        //实现从哪里点击登录，登录成功后到哪里去
        StringBuffer requestURL = request.getRequestURL();
        response.sendRedirect("http://localhost:9095?referer="+requestURL.toString());
        return false;
    }
}
