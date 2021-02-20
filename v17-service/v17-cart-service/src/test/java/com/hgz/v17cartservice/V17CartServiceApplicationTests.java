package com.hgz.v17cartservice;

import com.hgz.api.ICartService;
import com.hgz.commons.base.ResultBean;
import com.hgz.pojo.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17CartServiceApplicationTests {
    @Autowired
    private ICartService cartService;

    @Test
    public void addCartTest() {
        cartService.add("111",1L,100);
        cartService.add("111",2L,200);
        ResultBean resultBean = cartService.add("111", 1L, 300);
        System.out.println(resultBean.getData());
    }

    @Test
    public void queryCartTest(){
        ResultBean query = cartService.query("111");
        List<CartItem> list = (List<CartItem>) query.getData();
        for (CartItem cartItem : list) {
            System.out.println(cartItem);
        }
    }

    @Test
    public void deleteCartTest(){
        ResultBean resultBean = cartService.delete("111", 1L);
        System.out.println(resultBean);
    }

    @Test
    public void updateCartTest(){
        ResultBean resultBean = cartService.update("111", 1L, 500);
        System.out.println(resultBean);
    }

}
