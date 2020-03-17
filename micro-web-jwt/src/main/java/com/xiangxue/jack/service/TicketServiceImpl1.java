package com.xiangxue.jack.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl1 implements TicketService {

    @HystrixCommand
    @Override
    public String queryTicket() {
        return "queryTicket1";
    }

    @HystrixCommand
    @Override
    public String saveTicket() {
        return "saveTicket1";
    }
}
