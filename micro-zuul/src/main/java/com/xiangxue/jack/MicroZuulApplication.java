package com.xiangxue.jack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/*
* 界面：http://localhost:2020/hystrix
*
* */
@SpringBootApplication
@EnableZuulProxy
//@EnableZuulServer
public class MicroZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroZuulApplication.class,args);
    }
}
