package com.hgz.v17order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: DJA
 * @Date: 2019/11/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlipayBizContent {
    //商家订单号
    private String out_trade_no;
    //与支付宝签约的产品码
    private String product_code;
    //订单总金额
    private String total_amount;
    //订单标题
    private String subject;
    //订单描述
    private String body;
}
