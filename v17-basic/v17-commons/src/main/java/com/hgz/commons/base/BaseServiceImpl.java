package com.hgz.commons.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/10/29 11:11
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    public abstract IBaseDao<T> getBaseDao();

    @Override
    public int deleteByPrimaryKey(Long id) {
        return getBaseDao().deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T t) {
        return getBaseDao().insert(t);
    }

    @Override
    public int insertSelective(T t) {
        return getBaseDao().insertSelective(t);
    }

    @Override
    public T selectByPrimaryKey(Long id) {
        return getBaseDao().selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T t) {
        return getBaseDao().updateByPrimaryKeySelective(t);
    }

    @Override
    public int updateByPrimaryKey(T t) {
        return getBaseDao().updateByPrimaryKey(t);
    }

    @Override
    public List<T> list() {
        return getBaseDao().list();
    }

    @Override
    public PageInfo<T> page(Integer pageIndex, Integer pageSize) {
        //1.分页初始化
        PageHelper.startPage(pageIndex,pageSize);
        //2.收集集合数据
        List<T> list = this.list();   //分页插件，内部其实有limit
        //3.返回分页对象
        PageInfo<T> pageInfo = new PageInfo<>(list, 3);
        return pageInfo;
    }
}
