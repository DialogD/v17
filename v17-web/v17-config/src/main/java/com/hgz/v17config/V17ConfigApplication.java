package com.hgz.v17config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class V17ConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(V17ConfigApplication.class, args);
    }

}
