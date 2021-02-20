package com.hgz.v17searchservice;

import com.hgz.api.ISearchService;
import com.hgz.commons.base.PageResultBean;
import com.hgz.commons.base.ResultBean;
import com.hgz.entity.TProduct;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class V17SearchServiceApplicationTests {

    @Autowired
    private SolrClient solrClient;

    //目标： 实现对数据的增删改查

    @Autowired
    private ISearchService searchService;

    @Test
    public void queryPageKeywordsTest(){
        ResultBean resultBean = searchService.queryKeywordsPage("初级程序员", 1, 2);
        System.out.println(resultBean.getData().toString());

    }

    @Test
    public void synAllDataTest(){
        ResultBean resultBean = searchService.synAllData();
        System.out.println(resultBean.getData());
    }

    @Test
    public void synById(){
        ResultBean resultBean = searchService.synById(12L);
        System.out.println(resultBean.getData());
    }

    @Test
    public void queryKeywords(){
        ResultBean resultBean = searchService.queryKeywords("高级程序员");
        List<TProduct> list = (List<TProduct>) resultBean.getData();
        for (TProduct product : list) {
            System.out.println(product.getName());
        }
    }

    @Test
    public void addOrUpdateTest() throws IOException, SolrServerException {
        //1.创建document对象
        SolrInputDocument document = new SolrInputDocument();
        //2.给对象属性赋值
        document.setField("id",1);
        document.setField("product_name","小米9");
        document.setField("product_price","9999");
        document.setField("product_sale_point","像素最高的手机");
        document.setField("product_images","123");
        //3.保存、提交
        solrClient.add(document);
        solrClient.commit();
    }

    @Test
    public void queryTest() throws IOException, SolrServerException {
        //1.组装查询条件
        SolrQuery queryCondition = new SolrQuery();
        queryCondition.setQuery("product_name:小米4Max9");
        //2.执行查询
        QueryResponse response = solrClient.query(queryCondition);
        //3.得到结果
        SolrDocumentList solrDocuments = response.getResults();
        for (SolrDocument document : solrDocuments) {
            System.out.println(document.getFieldValue("product_name")+","+document.getFieldValue("product_price"));
        }
    }

    @Test
    public void deleteTest() throws IOException, SolrServerException {
//        solrClient.deleteByQuery("product_name:小米4Max9");
        solrClient.deleteById("1");
        solrClient.commit();
    }

}
