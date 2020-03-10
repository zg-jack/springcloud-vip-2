package com.xiangxue.jack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MicroJWTApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroJWTApplication.class, args);
    }
}
