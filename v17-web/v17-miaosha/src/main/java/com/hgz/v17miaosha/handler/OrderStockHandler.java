package com.hgz.v17miaosha.handler;

import com.hgz.commons.constant.MQConstant;
import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.mapper.TMiaoshaProductMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/23
 */
@Component
@Slf4j
public class OrderStockHandler {

    @Autowired
    private TMiaoshaProductMapper miaoshaProductMapper;

    @RabbitListener(queues = MQConstant.QUEUE.ORDER_STOCK_QUEUE)
    @RabbitHandler
    public void orderStockProcess(Map<String,Object> params){
        //获取到当前秒杀商品的Id
        Long seckillId = (Long) params.get("seckillId");
        //改变库存 -1
        TMiaoshaProduct product = miaoshaProductMapper.selectByPrimaryKey(seckillId);
        product.setCount(product.getCount()-1);
        product.setUpdateTime(new Date());
        int result = miaoshaProductMapper.updateByPrimaryKey(product);
    }
}
