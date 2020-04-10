package com.ilirus.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    @RequestMapping("/auth")
    public String authorize() {
        return "oauth/authorize";
    }

    @RequestMapping("/token")
    public String token(
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false)
            String error_description,
            Model model) {
        if(code != null) {
            model.addAttribute("code", code);
        }
        if(error != null) {
            model.addAttribute("error", "授权失败");
        }
        if(error_description != null) {
            model.addAttribute("error_description", error_description);
        }
        return "oauth/token";
    }
}
