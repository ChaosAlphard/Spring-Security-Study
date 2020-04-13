package com.ilirus.oauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/user")
    public String user() {
        return "user";
    }

    @RequestMapping("/res/r1")
    @PreAuthorize("hasRole('USER')")
    public String notFound() {
        return "访问资源r1";
    }
}
