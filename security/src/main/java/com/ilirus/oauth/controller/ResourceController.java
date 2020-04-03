package com.ilirus.oauth.controller;

import com.ilirus.oauth.utils.SecurityUtil;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    /* @Secured */
    // IS_AUTHENTICATED_ANONYMOUSLY: 表示可以匿名访问
    @Secured("IS_AUTHENTICATED_ANONYMOUSLY")
    @RequestMapping("/r")
    @ResponseBody
    public String resource() {
        return SecurityUtil.getUserName()+"访问资源";
    }

    // ROLE_<ROLE>: 表示需要有<ROLE>权限才能访问
    @Secured("ROLE_USER")
    @RequestMapping("/r1")
    @ResponseBody
    public String resource1() {
        return SecurityUtil.getUserName()+"访问资源1";
    }

    /* @PreAuthorize */
    // 在方法执行前进行权限验证
    // isAnonymous() 允许匿名访问
    @PreAuthorize("isAnonymous()")
    @RequestMapping("/pre/a")
    @ResponseBody
    public String preAssets() {
        return SecurityUtil.getUserName()+"访问preAssets";
    }

    // 拥有USER 权限才可访问
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("/pre/a1")
    @ResponseBody
    public String preAssets1() {
        return SecurityUtil.getUserName()+"访问preAssets1";
    }

    /* @PostAuthorize */
    // 在方法执行后进行权限验证
    // 拥有USER 和ADMIN 任一权限即可访问
    @PostAuthorize("hasAnyRole('USER', 'ADMIN')")
    @RequestMapping("/post/a1")
    @ResponseBody
    public String postAssets1() {
        String name = SecurityUtil.getUserName();
        System.out.println(name+"访问postAssets1");
        return name+"访问postAssets1";
    }

    // 同时拥有USER 和ADMIN 权限才可访问
    @PostAuthorize("hasRole('USER') and hasRole('ADMIN')")
    @RequestMapping("/post/a2")
    @ResponseBody
    public String postAssets2() {
        String name = SecurityUtil.getUserName();
        System.out.println(name+"访问postAssets2");
        return name+"访问postAssets2";
    }
}
