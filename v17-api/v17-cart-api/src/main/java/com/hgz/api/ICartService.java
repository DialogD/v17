package com.hgz.api;

import com.hgz.commons.base.ResultBean;

/**
 * @Author: DJA
 * @Date: 2019/11/14
 */
public interface ICartService {

    /**
     * 添加方法
     * @param token   唯一标识
     * @param productId  商品id
     * @param count    购买数量
     * @return
     */
    public ResultBean add(String token,Long productId,int count);

    public ResultBean delete(String token,Long productId);

    public ResultBean update(String token,Long productId,int count);

    public ResultBean query(String token);

    public ResultBean merge(String no_loginKey, String loginKey);
}
