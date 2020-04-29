package com.ilirus.oauth.jwt;

import com.ilirus.oauth.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.List;

/**
 * 授权过滤器
 */
@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = request.getHeader(JWTConfig.TOKEN_HEADER);
        // 如果没有authorization 请求头则直接放行
        if(authorization == null || !authorization.startsWith(JWTConfig.TOKEN_HEADER)){
            chain.doFilter(request,response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorization));
        super.doFilterInternal(request,response, chain);
    }


    /**
     * 从token中获取用户信息并新建token(排除掉密码)
     * @param authorization token
     * @return token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {
        log.debug("authorization: {}", authorization);
        // 去掉token中的"Bearer "字符
        String token = authorization.replace(JWTConfig.TOKEN_PREFIX, "");
        log.debug("replaced: {}", token);
        try {
            // 获取用户名
            String username = JwtUtil.getUsernameInToken(token);
            // 获取角色
            List<SimpleGrantedAuthority> roles = JwtUtil.getUserRolesInToken(token);
            if(!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username,null,roles);
            } else {
                log.warn("用户名解析失败");
            }
        } catch(ExpiredJwtException eje) {
            log.warn("token解析失败", eje);
        }
        return null;
    }
}
