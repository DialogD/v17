package com.hgz.v17springbootredis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: DJA
 * @Date: 2019/11/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductType implements Serializable {

    private Long id;
    private String name;
}
