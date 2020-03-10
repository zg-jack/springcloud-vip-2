package com.xiangxue.jack.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/security")
public class SecurityController {

    @RequestMapping(value = "/check", method = RequestMethod.GET)
    public Principal getUser(Principal principal) {
        log.info(principal.toString());
        return principal;
    }
}
