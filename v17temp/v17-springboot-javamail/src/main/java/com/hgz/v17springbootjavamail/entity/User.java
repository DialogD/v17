package com.hgz.v17springbootjavamail.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: DJA
 * @Date: 2019/11/10
 */
@Data
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
}
