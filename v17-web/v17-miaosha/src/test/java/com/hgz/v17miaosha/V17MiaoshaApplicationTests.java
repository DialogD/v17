package com.hgz.v17miaosha;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17MiaoshaApplicationTests {

    @Autowired
    private IMiaoShaService miaoShaService;

    @Test
    public void contextLoads() {
        TMiaoshaProduct product = miaoShaService.getById(1L);
        System.out.println(product.getProductName());
    }

}
