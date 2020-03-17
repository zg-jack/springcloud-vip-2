package com.xiangxue.jack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

@EnableZipkinServer
//@EnableEurekaClient
@SpringBootApplication
public class MicroZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroZipkinApplication.class,args);
    }
}
