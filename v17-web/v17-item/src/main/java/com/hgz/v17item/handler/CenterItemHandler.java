package com.hgz.v17item.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IProductService;
import com.hgz.commons.constant.MQConstant;
import com.hgz.v17item.controller.ItemController;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: DJA
 * @Date: 2019/11/10
 */
@Component
@RabbitListener(queues = MQConstant.QUEUE.CENTER_PRODUCT_EXCHANGE_ITEM_QUEUE)
@Slf4j
public class CenterItemHandler {

    @Autowired
    private ItemController itemController;

    @RabbitHandler
    public void process(Long id) throws IOException, TemplateException {
        log.info("处理了id=[{}]的消息",id);
        itemController.createById(id);
    }


}
