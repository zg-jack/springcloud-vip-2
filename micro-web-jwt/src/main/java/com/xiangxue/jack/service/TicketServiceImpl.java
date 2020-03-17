package com.xiangxue.jack.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl implements TicketService {

    @HystrixCommand
    @Override
    public String queryTicket() {
        return "queryTicket";
    }

    @HystrixCommand
    @Override
    public String saveTicket() {
        return "saveTicket";
    }
}
