package com.hgz.v17miaosha.service;


import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.pojo.ResultBean;

/**
 * @Author: DJA
 * @Date: 2019/11/20
 */
public interface IMiaoShaService {

    TMiaoshaProduct getById(Long id);

    ResultBean kill(Long userId, Long productId, String path);

    ResultBean getPath(Long userId, Long seckillId);
}
