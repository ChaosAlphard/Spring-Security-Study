package com.ilirus.oauth.jwt;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理授权用户访问更高一级权限的资源
 */
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e = new AccessDeniedException("权限不足");
        httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
    }
}
