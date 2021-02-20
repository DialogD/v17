package com.hgz.v17center.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.hgz.api.IProductService;
import com.hgz.commons.base.ResultBean;
import com.hgz.commons.constant.MQConstant;
import com.hgz.entity.TProduct;
import com.hgz.vo.ProductVO;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: DJA
 * @Date: 2019/10/28 22:25
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference(check = false)
    private IProductService productService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct getById(@PathVariable("id") Long id){
        System.out.println("id="+id);
        TProduct product = productService.selectByPrimaryKey(id);
        System.out.println("produceName="+product.getName());
        return product;
    }

    @RequestMapping("list")
    public String list(Model model){
        //获取数据
        List<TProduct> list = productService.list();
        model.addAttribute("list",list);
        return "product/list";
    }

    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String page(Model model,@PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize){
        //获取数据
        PageInfo<TProduct> page = productService.page(pageIndex, pageSize);
        model.addAttribute("page",page);
        return "product/list";
    }

    //配置mq回调处理函数
    //回调函数: confirm确认
    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            System.err.println("ack: " + ack);
            if(ack){
                System.out.println("说明消息正确送达MQ服务器");
                //获取到商品id
                System.out.println("productId: " + correlationData.getId());
                //TODO 2.更新消息的状态status
            }
        }
    };

    @RequestMapping("add")
    public String add(ProductVO vo){
        Long newId = productService.add(vo);

        //1.携带附加参数
        CorrelationData correlationData = new CorrelationData(newId.toString());
        //2.设置回调
        rabbitTemplate.setConfirmCallback(confirmCallback);
        //3.发送一个消息到中间件--信息同步搜索-详情
        rabbitTemplate.convertAndSend(MQConstant.EXCHANGE.CENTER_PRODUCT_EXCHANGE,"product.add",newId,correlationData);
        //TODO 1.往消息记录表插入数据
        //跳转回到第一页，或者按照添加时间排序
        return "redirect:/product/page/1/2";
    }

    //删除
    @RequestMapping("delete/{id}")
    @ResponseBody
    public ResultBean delete(@PathVariable("id") Long id){
        int count = productService.deleteByPrimaryKey(id);
        if (count>0){
            return new ResultBean("200","删除成功");
        }
        return new ResultBean("500","删除失败");

    }

    //批量删除
    @RequestMapping("delByIds")
    @ResponseBody
    public ResultBean delByIds(@RequestParam List<Long> ids){
        int count = productService.delByIds(ids);
        if (count>0){
            return new ResultBean("200","批量删除成功");
        }
        return new ResultBean("500","批量删除失败");
    }

    //toUpdate
    @RequestMapping("toUpdate/{id}")
    @ResponseBody
    public ResultBean toUpdate(@PathVariable("id") Long id){
        ProductVO vo = productService.getVOById(id);
//        System.out.println(vo.getProduct().getName()+","+vo.getProductDesc());
//        model.addAttribute("pro",vo.getProduct());
//        model.addAttribute("desc",vo.getProductDesc());
//        model.addAttribute("vo",vo);
        ResultBean<ProductVO> result = new ResultBean();
        result.setStatusCode("200");
        result.setData(vo);
        return result;
    }

    //修改update
    @RequestMapping("update")
    public String update(ProductVO vo){
        productService.update(vo);
        return "redirect:/product/page/1/2";
    }


}
