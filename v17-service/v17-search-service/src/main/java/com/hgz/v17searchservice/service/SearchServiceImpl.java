package com.hgz.v17searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.hgz.api.ISearchService;
import com.hgz.commons.base.PageResultBean;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import com.hgz.mapper.TProductMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: DJA
 * @Date: 2019/11/3
 */
@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private TProductMapper productMapper;

    @Override
    public ResultBean synAllData() {
        // TODO 合适吗？ 根据需求只需要查询所需要的列的数据
        //1.查询源数据
        List<TProduct> list = productMapper.list();
        //2.mysql-->solr
        for (TProduct product : list) {
            //product-->document
            SolrInputDocument document = new SolrInputDocument();
            //设置相关属性的值
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_sale_point",product.getSalePoint());
            document.setField("product_images",product.getImages());
            //3.保存
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                //TODO logo
                return new ResultBean("500","同步数据失败");
            }
        }
        //提交
        try {
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO logo
            return new ResultBean("500","同步数据失败");
        }
        return new ResultBean("200","同步数据成功");
    }

    @Override
    public ResultBean synById(Long id) {
        //1.查询源数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //2.mysql-->solr
        //product-->document
        SolrInputDocument document = new SolrInputDocument();
        //设置相关属性的值
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_images",product.getImages());
        //3.保存  //提交
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO logo
            return new ResultBean("500","同步数据失败");
        }

        return new ResultBean("200","同步数据成功");
    }

    @Override
    public ResultBean delById(Long id) {
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO logo
            return new ResultBean("500","同步数据失败");
        }
        return new ResultBean("200","同步数据成功");
    }

    @Override
    public ResultBean queryKeywords(String keywords) {
        //1.组装查询条件
        SolrQuery solrQueryCondition = new SolrQuery();
        if (keywords == null || "".equals(keywords.trim())){
            solrQueryCondition.setQuery("product_name:程序员");
        }else {
            solrQueryCondition.setQuery("product_name:"+keywords);
        }

        //设置高亮
        solrQueryCondition.setHighlight(true);
        solrQueryCondition.addHighlightField("product_name");
        solrQueryCondition.setHighlightSimplePre("<font color='red'>");
        solrQueryCondition.setHighlightSimplePost("</font>");

        //2.获取结果-->list
        List<TProduct> list = null;
        try {
            QueryResponse response = solrClient.query(solrQueryCondition);
            SolrDocumentList results = response.getResults();
            list = new ArrayList<>(results.size());
            //获取高亮
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : results) {
                //document-->product
               TProduct product = new TProduct();
               product.setId(Long.parseLong(document.getFieldValue("id").toString()));

               product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
               product.setSalePoint(document.getFieldValue("product_sale_point").toString());
               product.setImages(document.getFieldValue("product_images").toString());

               //针对高亮做设置
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighLighting = map.get("product_name");
                if (productNameHighLighting != null && productNameHighLighting.size()>0){
                    product.setName(productNameHighLighting.get(0));
                }else {
                    product.setName(document.getFieldValue("product_name").toString());
                }
                //TODO 合适吗? 传递product字段数和需要赋值的字段数不一样，其他的是空字段传输到前端  VO(view object)
               list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean("500",null);
        }
        return new ResultBean("200",list);
    }

    @Override
    public ResultBean queryKeywordsPage(String keywords, Integer pageIndex, Integer pageSize) {

        //1.组装查询条件
        SolrQuery solrQueryCondition = new SolrQuery();
        if (keywords == null || "".equals(keywords.trim())){
            solrQueryCondition.setQuery("product_name:华为");
        }else {
            solrQueryCondition.setQuery("product_name:"+keywords);
        }

        // 设置查询的排序参数，1-排序的字段名，2-排序方式（ORDER：asc desc）
        //默认是按着相关度(匹配度)排序-高到底
        solrQueryCondition.setSort("id", SolrQuery.ORDER.asc);
        //设置分页
        solrQueryCondition.setStart((pageIndex-1)*pageSize);   //起始页
        solrQueryCondition.setRows(pageSize);   //每页大小

        //设置高亮
        solrQueryCondition.setHighlight(true);
        solrQueryCondition.addHighlightField("product_name");
        solrQueryCondition.setHighlightSimplePre("<font color='red'>");
        solrQueryCondition.setHighlightSimplePost("</font>");

        //2.获取结果-->list
        List<TProduct> list = null;

        //分页：list-->pageResultBean
        PageResultBean<TProduct> page = new PageResultBean<>();
        long total = 0L;  //总记录数
        try {
            QueryResponse response = solrClient.query(solrQueryCondition);
            SolrDocumentList results = response.getResults();
            total = results.getNumFound();
            list = new ArrayList<>(results.size());
            //获取高亮
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : results) {
                //document-->product
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));

                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setImages(document.getFieldValue("product_images").toString());

                //针对高亮做设置--Map-->Map-->List
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighLighting = map.get("product_name");
                if (productNameHighLighting != null && productNameHighLighting.size()>0){
                    //高亮信息
                    product.setName(productNameHighLighting.get(0));
                }else {
                    //普通信息
                    product.setName(document.getFieldValue("product_name").toString());
                }
                //TODO 合适吗? 传递product字段数和需要赋值的字段数不一样，其他的是空字段传输到前端  VO(view object)
                list.add(product);
            }
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean("500",null);
        }

        //给分页赋值
        page.setPageNum(pageIndex);  //起始页
        page.setPageSize(pageSize);  //每页大小
        page.setTotal(total);   //总记录数
        page.setList(list);    //数据集合
        page.setPages((int) ((total%pageSize)==0?total/pageSize:total/pageSize+1));  //总页数
        page.setNavigatePages(3);  //导航页
        return new ResultBean("200",page);
    }


}
