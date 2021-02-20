package com.hgz.v17order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: DJA
 * @Date: 2019/11/18
 * 订单确认页，展示订单相关数据
 */
@Controller
@RequestMapping("order")
public class OrderController {

    @RequestMapping("toConfirm")
    public String toConfirm(){
        System.out.println("已进入订单确认页....");
        //1.获取当前用户信息
        //2.获取当前用户对应的购物车信息
        //3.获取当前用户的收件人信息
        //4.获取付款pay方式
        //5.获取物流供应商信息

        //跳转到页面展示
        return "confirm";
    }
}
