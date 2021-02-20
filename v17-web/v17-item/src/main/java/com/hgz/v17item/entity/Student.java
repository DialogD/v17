package com.hgz.v17item.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @Author: DJA
 * @Date: 2019/11/4
 */
@Data
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private Date createTime;

}
