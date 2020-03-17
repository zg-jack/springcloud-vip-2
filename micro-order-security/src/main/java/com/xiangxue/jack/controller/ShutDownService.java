package com.xiangxue.jack.controller;

import com.netflix.discovery.DiscoveryManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutDownService {

    /*
    手动下线：http://localhost:8763/eureka/apps/MICRO-ORDER/localhost:micro-order:8084

    * 把服务从服务列表中剔除，但是其实这个服务可能是好的
    * */
    @RequestMapping("/shutdown")
    public void shutdown() {
        DiscoveryManager.getInstance().shutdownComponent();
    }
}
