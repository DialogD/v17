package com.hgz.v17search.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hgz.api.ISearchService;
import com.hgz.commons.base.PageResultBean;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/11/3
 */
@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;

    @RequestMapping("synAllData")
    @ResponseBody
    public ResultBean synAllData(){
        return searchService.synAllData();
    }

    /**
     * 此接口适合于app端
     * 或者ajax异步加载到前端
     * @param keywords
     * @return
     */
    @RequestMapping("queryKeywords")
    @ResponseBody
    public ResultBean queryKeywords(String keywords){
        return searchService.queryKeywords(keywords);
    }

    /**
     * 适用于PC端
     * @param keywords
     * @param model
     * @return
     */
    //  log4j  ---for
//    @RequestMapping("queryKeywords4PC")
    public String queryKeywords4PC(String keywords, Model model){
        ResultBean resultBean = searchService.queryKeywords(keywords);
        if ("200".equals(resultBean.getStatusCode())){
            List<TProduct> list = (List<TProduct>) resultBean.getData();
            model.addAttribute("list",list);
        }
        return "list";
    }

    /**
     * 分页
     * @param keywords
     * @param model
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping("queryKeywords4PCPage/{pageIndex}/{pageSize}")
    public String queryKeywords4PCPage(String keywords, Model model,
                                       @PathVariable("pageIndex") Integer pageIndex,
                                       @PathVariable("pageSize") Integer pageSize){
        ResultBean resultBean = searchService.queryKeywordsPage(keywords,pageIndex,pageSize);
        if ("200".equals(resultBean.getStatusCode())){
            PageResultBean<TProduct> pageResultBean = (PageResultBean<TProduct>) resultBean.getData();
            model.addAttribute("page",pageResultBean);
            model.addAttribute("keywords",keywords);
            StringBuilder strKeywords = new StringBuilder("?keywords=").append(keywords);
            model.addAttribute("strKeywords",strKeywords);
        }
        return "list";
    }
}
