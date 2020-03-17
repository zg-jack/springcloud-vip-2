package com.xiangxue.jack.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.xiangxue.jack.bean.ZgOrder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static String SERVIER_NAME = "micro-order";

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "queryOrderByIdFallback",
            commandKey = "queryOrderById",
            groupKey = "querygroup-one",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
            },
            threadPoolKey = "queryOrderByIdhystrixJackpool")
    @Override
    public ZgOrder queryOrderById(String orderId) {
        ZgOrder results = restTemplate.getForObject("http://"
                + SERVIER_NAME + "/order/queryOrder?orderId=" + orderId, ZgOrder.class);
        return results;
    }

    @HystrixCommand(fallbackMethod = "addOrderFallback",
            commandKey = "addOrder",
            groupKey = "savegroup-one",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")
            },
            threadPoolKey = "addOrderhystrixJackpool")
    @Override
    public String addOrder(ZgOrder zgOrder) {
        Integer i = restTemplate.postForObject("http://"
                + SERVIER_NAME + "/order/addOrder", zgOrder,Integer.class);

        if( 1 == i) {
            return "保存成功";
        } else {
            return "保存失败";
        }
    }

    public ZgOrder queryOrderByIdFallback(String orderId) {
        log.info("===============queryOrderByIdFallback=================");
        return null;
    }

    public String addOrderFallback(ZgOrder zgOrder) {
        log.info("===============addOrderFallback=================");
        return null;
    }
}
