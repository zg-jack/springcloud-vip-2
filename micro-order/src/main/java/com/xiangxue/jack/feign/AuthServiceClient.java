package com.xiangxue.jack.feign;

import com.xiangxue.jack.config.JWT;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "micro-jwt", fallback = AuthServiceHystrix.class)
public interface AuthServiceClient {

    @PostMapping("/oauth/token")
    JWT getToken(@RequestHeader("Authorization") String authorization,
                 @RequestParam("grant_type") String type,
                 @RequestParam("username") String username,
                 @RequestParam("password") String password);
}
