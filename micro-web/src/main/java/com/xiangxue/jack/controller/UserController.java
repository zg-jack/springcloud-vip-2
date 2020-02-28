package com.xiangxue.jack.controller;

import com.xiangxue.jack.bean.ConsultContent;
import com.xiangxue.jack.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @RequestMapping("/queryUser")
    public List<ConsultContent> queryUser() {
        return userService.queryContents();
    }

    @RequestMapping("/queryContent")
    public List<ConsultContent> queryContent() {
        return userService.queryContent();
    }

    @RequestMapping("/queryMonitor")
    public String queryMonitor() {
        return userService.queryMonitor();
    }

    @RequestMapping("/loadBalancerClient")
    public String loadBalancerClient() {
        ServiceInstance choose = loadBalancerClient.choose("micro-order");
        log.info("====ServiceInstance:instanceId->" + choose.getInstanceId());
        log.info("====ServiceInstance:serviceId->" + choose.getServiceId());
        log.info("====ServiceInstance:ip@port@uri->" + choose.getHost()+"@"+choose.getPort()+"@"+choose.getUri());
        StringBuffer b = new StringBuffer();
        b.append("====ServiceInstance:serviceId->" + choose.getServiceId()).
                append("====ServiceInstance:serviceId->" + choose.getServiceId())
                .append("====ServiceInstance:ip@port@uri->" + choose.getHost()+"@"+choose.getPort()+"@"+choose.getUri());
        return b.toString();
    }

    // 能否访问数据库的标识
    public static boolean canVisitDb = true;

    @RequestMapping(value = "/db/{can}", method = RequestMethod.GET)
    public void setDb(@PathVariable boolean can) {
        canVisitDb = can;
    }
}
