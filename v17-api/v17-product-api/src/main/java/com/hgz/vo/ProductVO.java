package com.hgz.vo;

import com.hgz.entity.TProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: DJA
 * @Date: 2019/10/29 20:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVO implements Serializable {

    private TProduct product;
    private String productDesc;
}
