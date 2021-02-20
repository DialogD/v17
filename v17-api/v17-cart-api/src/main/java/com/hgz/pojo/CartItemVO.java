package com.hgz.pojo;

import com.hgz.entity.TProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: DJA
 * @Date: 2019/11/14
 * 与视图一一对应  view Object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemVO implements Serializable {

    private TProduct product;  //商品对象信息

    private Integer count;      //商品数量

    private Date updateTime;   //更新商品时间
}
