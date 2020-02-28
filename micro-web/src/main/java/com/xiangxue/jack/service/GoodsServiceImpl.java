package com.xiangxue.jack.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiangxue.jack.bean.ZgGoods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
public class GoodsServiceImpl implements GoodsService {

    public static String SERVIER_NAME = "micro-order";

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "addGoodsFallback",
            commandKey = "addGoods",
            groupKey = "savegroup-one",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
            },
            threadPoolKey = "addGoodshystrixJackpool")
    @Override
    public String addGoods(ZgGoods zgGoods) {
        Integer i = restTemplate.postForObject("http://"
                + SERVIER_NAME + "/goods/addGoods", zgGoods,Integer.class);

        if( 1 == i) {
            return "保存成功";
        } else {
            return "保存失败";
        }
    }

    @HystrixCommand(fallbackMethod = "queryAllFallback",
            commandKey = "queryAll",
            groupKey = "querygroup-one",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
            },
            threadPoolKey = "queryAllhystrixJackpool")
    @Override
    public List<ZgGoods> queryAll() {
        List<ZgGoods> results = restTemplate.getForObject("http://"
                + SERVIER_NAME + "/goods/queryGoods", List.class);
        return results;
    }

    public List<ZgGoods> queryAllFallback() {
        log.info("===============queryAllFallback=================");
        return null;
    }

    public String addGoodsFallback(ZgGoods zgGoods) {
        log.info("===============addGoodsFallback=================");
        return null;
    }
}
