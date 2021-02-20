package com.hgz.v17item;

import com.hgz.v17item.entity.Student;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class V17ItemApplicationTests {

    @Autowired
    private Configuration configuration;

    @Test
    public void createHTMLTest() throws IOException, TemplateException {
        //模板+数据===>输出
        //1.获取模板对象
        Template template = configuration.getTemplate("freemarker.ftl");
        //2.组装数据
        Map<String,Object> data = new HashMap<>();
        data.put("username","ftl");
        //保存对象
        Student student = new Student(1,"java入门到精通",new Date());
        data.put("student",student);
        //保存集合
        List<Student> list = new ArrayList<>();
        list.add(new Student(1,"C++",new Date()));
        list.add(new Student(2,"C",new Date()));
        data.put("list",list);
        //保存财富money
        data.put("money",10000);
        //3.模板+数据结合
        FileWriter out = new FileWriter("F:\\MavenIdea\\v17\\v17-web\\v17-item\\src\\main\\resources\\static\\hello.html");
        template.process(data,out);

        System.out.println("生成静态页面成功");
    }

}
