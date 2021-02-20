package com.hgz.v17productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.IProductService;
import com.hgz.commons.base.BaseServiceImpl;
import com.hgz.commons.base.IBaseDao;
import com.hgz.entity.TProduct;
import com.hgz.entity.TProductDesc;
import com.hgz.mapper.TProductDescMapper;
import com.hgz.mapper.TProductMapper;
import com.hgz.vo.ProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/10/28 21:44
 */
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService {

    @Autowired
    private TProductMapper productMapper;
    @Autowired
    private TProductDescMapper productDescMapper;

    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    @Override
    @Transactional   //事务控制
    public Long add(ProductVO vo) {
        //1.添加商品的基本信息
        productMapper.insertSelective(vo.getProduct());
        //2.添加商品的描述信息
        TProductDesc productDesc = new TProductDesc();
        productDesc.setProductId(vo.getProduct().getId());
        productDesc.setProductDesc(vo.getProductDesc());
        productDescMapper.insertSelective(productDesc);
        return vo.getProduct().getId();
    }

    @Override
    public int delByIds(List<Long> ids) {
        return productMapper.updateFlagToFalse(ids);
    }

    @Override
    public ProductVO getVOById(Long id) {
        TProduct product = productMapper.selectByPrimaryKey(id);
        String productDesc = productDescMapper.selectByProductId(id);
        ProductVO vo = new ProductVO();
        vo.setProduct(product);
        vo.setProductDesc(productDesc);
        return vo;
    }

    @Override
    @Transactional  //事务控制
    public int update(ProductVO vo) {
        //修改商品基本信息
        int count1 = productMapper.updateByPrimaryKeySelective(vo.getProduct());
        int count2 = productDescMapper.updateByProductId(vo.getProduct().getId(), vo.getProductDesc());
        System.out.println("count1="+count1+",count2="+count2);
        if (count1>0&&count2>0){
            return 1;
        }
        return 0;
    }
}
