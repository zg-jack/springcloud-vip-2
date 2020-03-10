package com.xiangxue.jack.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiangxue.jack.bean.ZgOrder;
import com.xiangxue.jack.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/queryOrder")
    public ZgOrder queryOrder(String orderId) {
        ZgOrder zgOrder = orderService.queryOrderById(orderId);
        return zgOrder;
    }

    @RequestMapping("/addOrder")
    public String addOrder(@RequestBody String param) {
        ZgOrder zgOrder = JSONObject.parseObject(param, ZgOrder.class);
        return orderService.addOrder(zgOrder);
    }
}
