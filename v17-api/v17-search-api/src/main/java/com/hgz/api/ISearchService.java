package com.hgz.api;

import com.hgz.commons.base.PageResultBean;
import com.hgz.commons.base.ResultBean;

/**
 * @Author: DJA
 * @Date: 2019/11/2 21:08
 */
public interface ISearchService {
    /**
     * 做全量同步
     * 数据初始化时使用
     * @return
     */
    public ResultBean synAllData();

    /**
     * 增量同步--当数据1000->1001时，增量为1使用
     * @param id
     * @return
     */
    public ResultBean synById(Long id);

    /**
     * 删除--根据id删除
     * @param id
     * @return
     */
    public ResultBean delById(Long id);

    /**
     * 查询操作
     * @param keywords
     * @return
     */
    public ResultBean queryKeywords(String keywords);


    /**
     * 分页查询操作
     * @param keywords
     * @param pageIndex
     * @param pageSize
     * @return
     */
    ResultBean queryKeywordsPage(String keywords, Integer pageIndex, Integer pageSize);
}
