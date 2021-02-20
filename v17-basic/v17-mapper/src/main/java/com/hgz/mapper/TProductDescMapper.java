package com.hgz.mapper;

import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProductDesc;
import org.apache.ibatis.annotations.Param;

public interface TProductDescMapper extends IBaseDao<TProductDesc> {
    //根据商品id查询描述信息
    String selectByProductId(Long id);
    //根据商品id修改描述信息
    int updateByProductId(@Param("id") Long id, @Param("productDesc") String productDesc);
}