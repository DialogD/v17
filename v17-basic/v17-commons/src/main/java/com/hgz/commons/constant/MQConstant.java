package com.hgz.commons.constant;

/**
 * @Author: DJA
 * @Date: 2019/11/7
 */
public interface MQConstant {

    public static class EXCHANGE{
        //用户中心-商品交换机
        public static final String CENTER_PRODUCT_EXCHANGE = "center-product-exchange";
        //短信服务交换机
        public static final String SMS_EXCHANGE = "sms-exchange";
        //邮箱服务交换机
        public static final String EMAIL_EXCHANGE = "email-exchange";
        //sso登录交换机
        public static final String SSO_EXCHANGE = "sso-exchange";
        //秒杀系统--生成订单-->订单系统交换机
        public static final String ORDER_EXCHANGE = "order-exchange";
    }

    public static class QUEUE{

        public static final String CENTER_PRODUCT_EXCHANGE_SEARCH_QUEUE = "center-product-exchange-search-queue";

        public static final String CENTER_PRODUCT_EXCHANGE_ITEM_QUEUE = "center-product-exchange-item-queue";

        //短信服务的队列
        public static final String SMS_CODE_QUEUE ="sms-code-queue";
        public static final String SMS_BIRTHDAY_QUEUE ="sms-birthday-queue";

        //邮箱服务的队列
        public static final String EMAIL_BIRTHDAY_QUEUE ="email-birthday-queue";
        public static final String EMAIL_ACTIVATE_QUEUE ="email-activate-queue";

        //登录服务的队列
        public static final String SSO_QUEUE_CART = "sso-queue-cart";

        //订单服务的队列
        public static final String ORDER_QUEUE = "order-queue";
        //用户秒杀抢购成功Mysql减少库存队列-1
        public static final String ORDER_STOCK_QUEUE = "order-stock-queue";
    }
}
