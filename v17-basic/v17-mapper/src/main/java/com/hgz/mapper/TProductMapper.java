package com.hgz.mapper;

import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProduct;

import java.util.List;

public interface TProductMapper extends IBaseDao<TProduct> {

    public int updateFlagToFalse(List<Long> ids);

}