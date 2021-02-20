package com.hgz.v17miaosha.service;

import com.hgz.v17miaosha.pojo.ResultBean;

/**
 * @Author: DJA
 * @Date: 2019/11/27
 */
public interface IRemindService {
    ResultBean add(Integer txseckillId, Integer userId);
}
