package com.hgz.api;

import com.hgz.commons.base.IBaseService;
import com.hgz.entity.TProduct;
import com.hgz.vo.ProductVO;

import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/10/28 21:41
 */
public interface IProductService extends IBaseService<TProduct> {

    //添加vo
    public Long add(ProductVO vo);  //返回主键id,Long类型
    //批量删除
    public int delByIds(List<Long> ids);
    //根据商品id获取vo信息
    public ProductVO getVOById(Long id);
    //修改
    public int update(ProductVO vo);

}
