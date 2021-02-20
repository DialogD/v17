package com.hgz.v17productservice;

import com.github.pagehelper.PageInfo;
import com.hgz.api.IProductService;
import com.hgz.api.IProductTypeService;
import com.hgz.entity.TProduct;
import com.hgz.entity.TProductType;
import com.hgz.vo.ProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17ProductServiceApplicationTests {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductTypeService productTypeService;

    @Autowired
    private DataSource dataSource;

    @Test
    public void dataSourceTest() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    public void typeList(){
        List<TProductType> list = productTypeService.list();
        for (TProductType productType : list) {
            System.out.println(productType.getName());
        }
    }

    @Test
    public void contextLoads() {
        TProduct product = productService.selectByPrimaryKey(1L);
        System.out.println(product.getName()+"===="+product.getSalePoint());
    }

    @Test
    public void list(){
        List<TProduct> list = productService.list();
        for (TProduct product : list) {
            System.out.println(product.getName());
        }
        Assert.assertNotNull(list);
    }

    @Test
    public void page(){
        PageInfo<TProduct> pageInfo = productService.page(1, 1);
        System.out.println("长度="+pageInfo.getList().size());
    }

    @Test
    public void addVO(){
        TProduct product = new TProduct();
        product.setName("格力手机");
        product.setPrice(2999L);
        product.setSalePrice(1999L);
        product.setSalePoint("超厉害");
        product.setImages("123");
        product.setTypeId(1);
        product.setTypeName("电子数码");
        ProductVO vo = new ProductVO();
        vo.setProduct(product);
        vo.setProductDesc("超强");
        System.out.println(productService.add(vo));
    }

    @Test
    public void delByIds(){
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        ids.add(3L);
        System.out.println("批量删除返回结果="+productService.delByIds(ids));
    }

    @Test
    public void getVOById(){
        ProductVO vo = productService.getVOById(3L);
        System.out.println(vo.getProduct().getName()+","+vo.getProductDesc());
    }

    @Test
    public void update(){
        TProduct product = new TProduct();
        product.setId(6L);
        product.setName("格力手机2");
        product.setPrice(2998L);
        product.setSalePrice(1998L);
        product.setSalePoint("超厉害");
        product.setImages("555");
        product.setTypeId(1);
        product.setTypeName("电子数码");
        ProductVO vo = new ProductVO();
        vo.setProduct(product);
        vo.setProductDesc("超强aaa");
        int update = productService.update(vo);
        System.out.println("结果update="+update);

    }

}
