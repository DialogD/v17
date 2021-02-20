package com.hgz.v17order.handler;

import com.hgz.commons.constant.MQConstant;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/21
 */
@Component
public class OrderHandler {

    //用户抢购成功，异步生成订单，通过map传递生成订单需要的参数
    //user_id,product_id,count(1),product_price,orderNo
    @RabbitListener(queues = MQConstant.QUEUE.ORDER_QUEUE)
    @RabbitHandler
    public void processMiaosha(Map<String,Object> params,
                               Channel channel,
                               Message message){
        System.out.println(params);
        System.out.println(params.get("orderNo"));
        //TODO 生成订单信息，状态为未支付，设置定时任务，若30min没有支付则解锁库存
        // 异步写(与活动结束未秒杀完的异步写类似)将秒杀商品存到商品服务的总库存中
        // 延时队列

        try {
            //假设耗时较长
            System.out.println("模拟生成订单表和订单详情表的相关信息...");
            Thread.sleep(5000);

            //处理完业务逻辑之后，手工确认消息已经被消费
            long tag = message.getMessageProperties().getDeliveryTag();
            //multiple:是否批量处理
            channel.basicAck(tag,false);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }

    }
}
