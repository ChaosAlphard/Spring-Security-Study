package com.ilirus.oauth.jwt;

import com.ilirus.oauth.entities.UserLogin;
import com.ilirus.oauth.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 认证过滤器
 */
@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private ThreadLocal<Boolean> remember = new ThreadLocal<>();
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        // 设置登录请求的 URL
        super.setFilterProcessesUrl(JWTConfig.AUTH_LOGIN_URL);
    }

    // 验证用户身份
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // 从输入流中获取到登录的信息
            UserLogin login = objectMapper.readValue(request.getInputStream(), UserLogin.class);
            remember.set(login.getRemember());
            // 用户名和密码写入到
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch(IOException ioe) {
            log.warn("读取登录信息失败", ioe);
            return null;
        }
    }

    // 验证成功
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
        List<String> roles = jwtUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // 生成token
        String token = JwtUtil.generateToken(jwtUser.getUsername(), roles, remember.get());
        // Response Header 中返回token
        response.setHeader(JWTConfig.TOKEN_HEADER, token);
    }

    // 验证失败
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}
