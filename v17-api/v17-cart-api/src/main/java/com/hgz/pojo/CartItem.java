package com.hgz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: DJA
 * @Date: 2019/11/14
 * 存入redis中购物车结构一一对应
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable,Comparable<CartItem> {

    private Long productId;

    private Integer count;

    private Date updateTime;

    @Override
    public int compareTo(CartItem o) {
        //排序，时间降序排序，需要考虑到时间相同的情况
        int result = (int) (o.updateTime.getTime() - this.updateTime.getTime());
        if (result == 0){  //时间相同情况下，不能覆盖
            return -1;
        }
        return result;
    }
}
