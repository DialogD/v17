package com.hgz.v17cart.interceptor;

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
 * @Date: 2019/11/16
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Reference
    private IUserService userService;

    /**
     * 重写前置拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否是登录状态,都放行
        //1. 若是登录状态，则保存当前用户信息到request中
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if ("user_token".equals(cookie.getName())){
                    ResultBean resultBean = userService.checkIsLogin(cookie.getValue());
                    if ("200".equals(resultBean.getStatusCode())){
                        //进入这里说明已经登录,保存信息，data:username
                        request.setAttribute("user",resultBean.getData());
                        return true;
                    }
                }
            }
        }
        return true;
    }
}
