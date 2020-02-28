package com.xiangxue.jack.config;

import com.xiangxue.ribbon.config.RibbonLoadBalanceMicroOrderConfig;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/*
* 这个是针对 micro-order服务的 ribbon配置
* */
@Configuration
@RibbonClients(value = {
        @RibbonClient(name = "micro-order",configuration = RibbonLoadBalanceMicroOrderConfig.class)
})
public class LoadBalanceConfig {

}
