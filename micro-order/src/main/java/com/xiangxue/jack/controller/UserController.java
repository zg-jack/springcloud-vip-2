package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.ConsultContent;
import com.xiangxue.jack.config.UserLoginDTO;
import com.xiangxue.jack.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RefreshScope
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

    @Value("${db.password}")
    private String dbpass;

    @Autowired
    Environment environment;

    @RequestMapping("/queryContent")
    public List<ConsultContent> queryContent(HttpServletRequest request) {
        logger.info(""+this.hashCode());
        logger.info("==================已经调用==========" + request.getRemotePort());
        logger.info("@Value======username======" + username);
        logger.info("Environment======username======" + environment.getProperty("username"));
        logger.info("@Value======redispass======" + redispass);
        logger.info("Environment======redispass======" + environment.getProperty("redis.password"));


        logger.info("zookeeper======zk.jack.url======" + environment.getProperty("configzk.jack.url8"));
        return userService.queryContent();
    }

    @PostMapping("/register")
    public String postUser(@RequestParam("username") String username,
                         @RequestParam("password") String password){
        int i = userService.insertUser(username, password);
        return i == 1 ? "S" : "F";
    }

    @PostMapping("/login")
    public UserLoginDTO login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        return userService.login(username,password);
    }
}
