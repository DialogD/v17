package com.hgz.v17springbootredis;

import com.hgz.v17springbootredis.entity.ProductType;
import com.hgz.v17springbootredis.filter.GoogleBloomFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class V17SpringbootRedisApplicationTests {

    @Resource(name = "redisTemplate1")
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private GoogleBloomFilter googleBloomFilter;

    @Test
    public void contextLoads() {
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Object k1 = redisTemplate.opsForValue().get("k1");
        System.out.println(k1);
    }

    @Test
    public void cacheTest() throws InterruptedException {
        List<ProductType> types = (List<ProductType>) redisTemplate.opsForValue().get("productType:list");

        //synchronized (Object.class)---->分布式锁(第三方)
        if (types == null) {  //T1  T2  T3
            //ADD 尝试获取到redis中的分布式锁
            String lockValue = UUID.randomUUID().toString();
            //redis4.0+  --> 新特性
            //redisTemplate.opsForValue().setIfAbsent("lock",lockValue,1,TimeUnit.MINUTES);

            //扩展redis的指令---lua脚本
            Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent("lock", lockValue);
            //TODO 作为一个整体来看，获取锁和设置有效时间两个操作不是原子操作
            if (ifAbsent){
                //设置有效时间，避免死锁
                redisTemplate.expire("lock",1,TimeUnit.MINUTES);

                log.info("当前缓存不存在数据....");
                Thread.sleep(1);

                log.info("从数据库中取数据");
                types = new ArrayList<>();
                types.add(new ProductType(1L,"7天精通java"));
                types.add(new ProductType(2L,"7天精通C++"));

                redisTemplate.opsForValue().set("productType:list",types);
                //当T1执行超过一分钟,T2进入，而此时T1释放了T2的锁-->唯一性标识判断
                //释放锁-->无锁
                //ADD  释放之前判断是否是自己的锁
                String currentLockValue = (String) redisTemplate.opsForValue().get("lock");
                if (lockValue.equals(currentLockValue)){
                    redisTemplate.delete("lock");
                }
                return;
            }

        }

        log.info("缓存中存在，直接从缓存中取数据");
    }

    @Test
    public void luaTest(){
        //1.创建一个可以执行lua脚本的执行对象
        DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
        //2.获取要执行的脚本
        lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock.lua")));
        //3.设置返回类型
        lockScript.setResultType(Boolean.class);
        //4.封装参数
        List<String> keyList = new ArrayList<>();
        keyList.add("lock");
        String uuid = UUID.randomUUID().toString();
        keyList.add(uuid);
        keyList.add("60");
        //5.执行脚本--execute
        Boolean result = redisTemplate.execute(lockScript, keyList);
        System.out.println(result);
    }

    //在多线程开发中，上述逻辑是否存在问题？
    @Test
    public void threadTest(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(4,8,1,
                TimeUnit.SECONDS,new LinkedBlockingQueue<>(100));
        for (int i = 0; i < 100; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        cacheTest();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //主线程不能结束
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //1. 演示穿透攻击的问题
    //缓存没有起到保护数据库的作用，直接被架空了
    //每一次请求都打到数据库中，简称穿透攻击

    //2.如何解决问题？

    //布隆过滤器

    public void penetrationAttackTest(){
        //查询id=1001的商品类别数据
        ProductType productType = (ProductType) redisTemplate.opsForValue().get("productType:1001");
        if (productType == null){
            //上锁--有效时间
            //查询数据库(如果数据库查询不到符合条件的数据，那么会怎么样？)
            log.info("执行数据库查询操作...");
            //加入缓存(没东西可放)
            //释放锁
        }else {
            //直接从缓存中获取数据
            log.info("从缓存中取数据...");
        }
    }

    @Test
    public void bloomFilterTest(){
        List<Boolean> result = new ArrayList<>();

        for (long i = 101; i < 1100; i++) {
            boolean exists = googleBloomFilter.isExists(i);
            if (exists){  //true表示误判
                result.add(exists);
            }
        }
        System.out.println(result.size());
    }

    @Test
    public void redisBloomTest(){

    }
}
