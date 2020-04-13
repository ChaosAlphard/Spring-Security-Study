package com.ilirus.oauth.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class IndexController {
    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return "index";
    }

    @RequestMapping("/error/**")
    public String notFound() {
        return "index";
    }

    @RequestMapping("/login")
    public String login(String error, String logout, String INVALID_SESSION, Model model) {
        if(error != null) {
            model.addAttribute("err", "认证失败");
        }
        if(logout != null) {
            model.addAttribute("logout", "你已注销");
        }
        if(INVALID_SESSION != null) {
            model.addAttribute("invalid", "session失效");
        }
        return "login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }

    @RequestMapping("/user")
    public String user(Model model) {
        String username = this.getUserName();
        model.addAttribute("username",username);
        return "user";
    }

    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return "游客";
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            // anonymousUser: 匿名用户
            return principal.toString();
        }
    }
}
