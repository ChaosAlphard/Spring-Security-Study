package com.ilirus.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String error, String logout, Model model) {
        if(error != null) {
            model.addAttribute("err", "认证失败");
        }
        if(logout != null) {
            model.addAttribute("logout", "你已注销");
        }
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }

    @RequestMapping("/user")
    public String user() {
        return "user";
    }
}
