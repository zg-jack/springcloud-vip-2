package com.xiangxue.jack.feign;

import com.xiangxue.jack.config.JWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AuthServiceHystrix implements AuthServiceClient {

    @Override
    public JWT getToken(String authorization, String type, String username, String password) {
        log.warn("Fallback of getToken is executed");
        return null;
    }
}
