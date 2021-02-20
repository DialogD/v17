package com.hgz.v17item.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.IProductService;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @Author: DJA
 * @Date: 2019/11/4
 */
@Controller
@RequestMapping("item")
@Slf4j
public class ItemController {

    @Reference(check = false)
    private IProductService productService;

    @Autowired
    private Configuration configuration;

    @Autowired
    private ThreadPoolExecutor pool;



    @RequestMapping("createById/{id}")
    @ResponseBody
    public ResultBean createById(@PathVariable("id") Long id) throws IOException, TemplateException {
        create(id);
        return new ResultBean("200","生成静态页面成功");
    }

    private void create(@PathVariable("id") Long id) throws IOException, TemplateException {
        //1.根据id商品信息
        TProduct product =
                productService.selectByPrimaryKey(id);
        //2.采用freemarker生成相应的详情页
        Template template = null;

        template = configuration.getTemplate("item.ftl");
        Map<String,Object> data = new HashMap<>();
        data.put("product",product);
        //3.模板和数据组合输出
        //获取到项目运行时的路径
        //获取static路径
        String serverPath = ResourceUtils.getURL("classpath:static").getPath();
        StringBuilder path = new StringBuilder(serverPath).append(File.separator).append(product.getId()).append(".html");
        FileWriter out = new FileWriter(path.toString());
        template.process(data,out);
    }

    //批量生成静态页面--单线程--》多线程
    @RequestMapping("batchCreate")
    @ResponseBody
    public ResultBean batchCreate(@RequestParam List<Long> ids) {

        //TODO 不足之处：每次都是创建一个新的连接池--单例
        //获取当前服务器的CPU核数
//        int processors = Runtime.getRuntime().availableProcessors();
        //结论：自己创建线程池   ThreadPoolExecutor对象来创建
//        ThreadPoolExecutor pool = new ThreadPoolExecutor(processors, processors * 2, 1L, TimeUnit.SECONDS,
//                new LinkedBlockingDeque<>(100));
        List<Future<Long>> results = new ArrayList<>(ids.size());  //返回结果信息
        //串行-->并行
        for (Long id : ids) {
            //create(id);   //单线程模式

            //提交一个任务给线程池去调用线程并且执行
            Future<Long> future = pool.submit(new CreateHtmlTask(id));
            results.add(future);
        }

        //后续错误结果处理的集合
        List<Long> errors = new ArrayList<>();
        for (Future<Long> future : results) {
            try {
                Long result = future.get();
                if (result != 0){
                    errors.add(result);  //添加失败的id信息到集合中
                }
            } catch (InterruptedException | ExecutionException e) {
                //如果我们针对不同的异常，处理方式是不同的，那么则需要分别catch
                e.printStackTrace();
            }

        }

        //打印error信息
        for (Long error : errors) {
            //System.out.println("errorProductId="+error);

            //做错误处理的工作
            //1.输出日志
            log.error("批量生成页面失败，失败的页面为：[{}]",errors);
            //2.将处理错误的id信息保存到日志表中
            // id  product_id retry_times create_time update_time
            // 1      1          0           2019.11.05   2019

            //3.通过定时任务，扫描这张表
            //select * from t_create_html_log where retry_times<3;
            //update retry_times=retry_time-1

            //4.超过3次的记录，需要人工介入(发短信或邮件，后台处理信息菜单...)
        }
        return new ResultBean("200","批量生成静态页面成功");
    }


    private class CreateHtmlTask implements Callable<Long>{

        private long id;

        public CreateHtmlTask(long id){
            this.id = id;
        }

        /**
         * 当成功时-返回0
         * 当执行失败时-则返回当前调用的id
         * @return
         * @throws IOException
         */
        @Override
        public Long call() throws IOException {
            //1.根据id商品信息
            TProduct product =
                    productService.selectByPrimaryKey(id);
            //2.采用freemarker生成相应的详情页
            Template template = null;
            try {
                template = configuration.getTemplate("item.ftl");
                Map<String,Object> data = new HashMap<>();
                data.put("product",product);
                //3.模板和数据组合输出
                //获取到项目运行时的路径
                //获取static路径
                String serverPath = ResourceUtils.getURL("classpath:static").getPath();
                StringBuilder path = new StringBuilder(serverPath).append(File.separator).append(product.getId()).append(".html");
                FileWriter out = new FileWriter(path.toString());
                template.process(data,out);
            } catch (IOException | TemplateException e) {
                e.printStackTrace();
                return id;
            }
            return 0L;
        }
    }


}

