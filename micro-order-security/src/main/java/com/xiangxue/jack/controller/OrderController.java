package com.xiangxue.jack.controller;

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
        return orderService.queryOrderById(orderId);
    }

    @RequestMapping("/addOrder")
    public int addOrder(@RequestBody ZgOrder zgOrder) {
        return orderService.addOrder(zgOrder);
    }
}
