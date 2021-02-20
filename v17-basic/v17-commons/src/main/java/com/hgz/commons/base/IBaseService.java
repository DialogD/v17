package com.hgz.commons.base;


import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/10/29 11:08
 */
public interface IBaseService<T> {
    int deleteByPrimaryKey(Long id);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);

    List<T> list();

    //分页
    PageInfo<T> page(Integer pageIndex, Integer pageSize);
}
