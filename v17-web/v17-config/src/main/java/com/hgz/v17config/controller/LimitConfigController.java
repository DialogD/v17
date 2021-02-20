package com.hgz.v17config.controller;

import com.hgz.commons.base.ResultBean;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: DJA
 * @Date: 2019/11/23
 */
@Controller
@RequestMapping("config")
public class LimitConfigController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //配置mq回调处理函数
    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                //到底确认那个消息(队列)被确认了？
                System.out.println("correlationData: " + correlationData.getId());
                //TODO 2.更新消息的状态status
            }
        }
    };

    //可视化配置LimiterRate
    @RequestMapping("changeRate/{rate}")
    @ResponseBody
    public ResultBean changeRate(@PathVariable("rate") Double rate){

        //设置消息异步处理回调函数
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //添加一些附加相关参数
        CorrelationData correlationData = new CorrelationData(rate.toString());
        //TODO 1.往消息记录表插入数据
        //异步发送消息
        rabbitTemplate.convertAndSend("limit-exchange","limit.change",rate,correlationData);
        return new ResultBean("200","异步发送配置令牌消息成功"+rate);
    }

    //TODO 3.定时任务，扫描消息记录表，进行补偿重发
}
