package com.xiangxue.jack;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = { "com.xiangxue.jack" })
// 注册到eureka
@EnableEurekaClient
@MapperScan("com.xiangxue.jack.dao")
public class MicroOrderSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroOrderSecurityApplication.class, args);
    }
}
