package com.xiangxue.jack.routeMapper;

import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;

//@Configuration
public class PatternServiceRouteMapperConfig {

//    @Bean
    public PatternServiceRouteMapper patternServiceRouteMapper() {

        /*
         * servicePattern 指定微服务的正则
         * routePattern  指定路由正则
         * */
        return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)",
                "${version}/${name}");
    }
}
