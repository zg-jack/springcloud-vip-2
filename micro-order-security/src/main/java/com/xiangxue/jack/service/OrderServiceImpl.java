package com.xiangxue.jack.service;

import com.xiangxue.jack.bean.ZgOrder;
import com.xiangxue.jack.dao.ZgOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    ZgOrderMapper zgOrderMapper;

    private static  final String url = "http://127.0.0.1:8080/deliver?orderId=";

    @Override
    public ZgOrder queryOrderById(String orderId) {
        List<ZgOrder> zgOrders = zgOrderMapper.queryOrderByOrderId(orderId);
        if(zgOrders != null && zgOrders.size() == 1) {
            return zgOrders.get(0);
        }
        return null;
    }

    @Transactional
    @Override
    public int addOrder(ZgOrder zgOrder) {
        return zgOrderMapper.addOrder(zgOrder);
    }
}
