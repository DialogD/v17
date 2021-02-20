package com.hgz.v17springbootredis.filter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DJA
 * @Date: 2019/11/30
 */
@Component
public class GoogleBloomFilter {

    private BloomFilter bloomFilter;

    //TODO 改造redis版本

    //在容器启动的时候没回自动执行该方法，且只执行一遍
    @PostConstruct
    public void initData(){
        //1.模拟数据的初始化数据
        List<Long> list = new ArrayList<>();
        for (long i = 1; i <= 100; i++) {
            list.add(i);
        }

        //2.初始化布隆过滤器 --默认为0.03
        bloomFilter = BloomFilter.create(Funnels.longFunnel(),list.size(),0.001);

        //3.将数据放入布隆过滤器
        for (Long l : list) {
            //add.lua
            bloomFilter.put(l);
        }
    }

    public boolean isExists(Long data){
        // exists.lua
        return bloomFilter.mightContain(data);
    }
}
