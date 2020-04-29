package com.ilirus.oauth.jwt;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理匿名用户访问需要权限的资源
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     *
     * @param httpServletRequest  request
     * @param httpServletResponse response
     * @param e                   exception
     * @throws IOException IOException
     * @throws ServletException ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
