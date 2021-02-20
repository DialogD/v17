package com.hgz.v17miaosha.controller;

import com.hgz.v17miaosha.entity.TMiaoshaProduct;
import com.hgz.v17miaosha.exception.MiaoShaException;
import com.hgz.v17miaosha.pojo.ResultBean;
import com.hgz.v17miaosha.service.IMiaoShaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: DJA
 * @Date: 2019/11/20
 */
@Controller
@RequestMapping("miaosha")
public class MiaoShaController {

    @Autowired
    private IMiaoShaService miaoShaService;

    @RequestMapping("get")
    public String getById(Long id, Model model){
        TMiaoshaProduct miaoshaProduct = miaoShaService.getById(id);
        model.addAttribute("product",miaoshaProduct);
        return "miaosha_detail";
    }

    //获取真实的秒杀地址
    //1.客户端发起请求，获取动态的path(前提也是要用户已经登录和到了秒杀时间)
    //保存 miaosha:userId:seckillId---path保存到redis
    /**
     * @param userId  当前用户id
     * @param seckillId  当前参与活动id
     * @return
     */
    @GetMapping("getPath")
    @ResponseBody
    public ResultBean getPath(Long userId,Long seckillId){
        try {
            return miaoShaService.getPath(userId,seckillId);
        }catch (MiaoShaException e){
            return new ResultBean("404",e.getMessage());
        }

    }


    //不直接暴露秒杀地址，用户通过getPath获取到用户真正的动态秒杀地址
    //根据动态生成的path,重新发起第二次请求（执行真正的秒杀活动）
    @RequestMapping("kill/{path}")
    @ResponseBody
    public ResultBean kill(Long userId, Long seckillId,
                           @PathVariable("path") String path){

        try{
            return miaoShaService.kill(userId,seckillId,path);
        }catch (MiaoShaException e){
            //e.getMessage():Service层具体失败的原因信息
            return new ResultBean("404",e.getMessage());
        }
    }

    /*@RequestMapping("kill")
    @ResponseBody
    public ResultBean kill(Long userId,Long id){
        //TODO 调用用户服务，获取用户信息
        //假设获取为1的用户信息
//        Long userId = 1L;
        try{
            return miaoShaService.kill(userId,id);
        }catch (MiaoShaException e){
            //e.getMessage():Service层具体失败的原因信息
            return new ResultBean("404",e.getMessage());
        }
    }*/
}
