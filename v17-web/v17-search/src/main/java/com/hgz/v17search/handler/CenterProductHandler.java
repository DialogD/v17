package com.hgz.v17search.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ISearchService;
import com.hgz.commons.constant.MQConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @Author: DJA
 * @Date: 2019/11/7
 */
@Component
@RabbitListener(queues = MQConstant.QUEUE.CENTER_PRODUCT_EXCHANGE_SEARCH_QUEUE)
@Slf4j
public class CenterProductHandler {

    //1.声明队列  center-product-exchange-search-queue
    //2.绑定交换机

    //方案1：借助管理平台

    @Reference
    private ISearchService searchService;

    @RabbitHandler
    public void process(Long id){
        log.info("处理了id=[{}]的消息",id);
        searchService.synById(id);
    }
}
