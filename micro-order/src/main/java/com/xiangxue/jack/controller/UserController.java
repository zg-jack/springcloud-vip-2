package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.ConsultContent;
import com.xiangxue.jack.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @Value("${username}")
    private String username;

    @Value("${redis.password}")
    private String redispass;

    @Autowired
    Environment environment;

    @RequestMapping("/queryContent")
    public List<ConsultContent> queryContent(HttpServletRequest request) {
        logger.info("==================已经调用==========" + request.getRemotePort());
        logger.info("@Value======username======" + username);
        logger.info("Environment======username======" + environment.getProperty("username"));
        logger.info("@Value======redispass======" + redispass);
        logger.info("Environment======redispass======" + environment.getProperty("redis.password"));
        return userService.queryContent();
    }
}
