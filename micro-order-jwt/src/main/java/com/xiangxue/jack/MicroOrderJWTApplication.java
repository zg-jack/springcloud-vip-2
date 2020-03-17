package com.xiangxue.jack;

import com.xiangxue.jack.druidConfig.DruidConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = { "com.xiangxue.jack" })
// 注册到eureka
@EnableEurekaClient
@MapperScan("com.xiangxue.jack.dao")
@EnableFeignClients
@EnableConfigurationProperties(DruidConfig.class)
public class MicroOrderJWTApplication {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setConnectionRequestTimeout(2000);
//        factory.setReadTimeout(4000);
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroOrderJWTApplication.class, args);
	}

}
