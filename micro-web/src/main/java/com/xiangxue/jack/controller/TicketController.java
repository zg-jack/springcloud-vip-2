package com.xiangxue.jack.controller;

import com.xiangxue.jack.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    @Qualifier("ticketServiceImpl")
    private TicketService ticketService;

    @Autowired
    @Qualifier("ticketServiceImpl1")
    private TicketService ticketService1;

    @RequestMapping("/saveTicket")
    public String saveTicket() {
        return ticketService.saveTicket() + "->" + ticketService1.saveTicket();
    }

    @RequestMapping("/queryTicket")
    public String queryTicket() {
        return ticketService.queryTicket() + "->" + ticketService1.queryTicket();
    }
}
