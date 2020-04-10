package com.ilirus.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    // 配置客户端详情信息
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()  //暂时使用内存方式
            // client_id
            .withClient("c1")
            // 客户端密钥
            .secret(new BCryptPasswordEncoder().encode("secret"))
            // 客户端可以访问的资源列表
            .resourceIds("res1")
            // 允许的授权类型
            .authorizedGrantTypes("authorization_code", "password",
                "client_credentials", "implicit", "refresh_token")
            // 允许的授权范围
            .scopes("all")
            // false: 如果是授权码模式会自动跳转到授权页面
            .autoApprove(false)
            // 加上验证回调地址
            .redirectUris("http://127.0.0.1:80/token");
    }

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    // 令牌管理服务配置
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        // 设置客户端信息服务
        services.setClientDetailsService(clientDetailsService);
        // 是否产生刷新令牌
        services.setSupportRefreshToken(true);
        // 令牌储存策略
        services.setTokenStore(tokenStore);
        // 令牌默认有效期2小时 2*60*60
        services.setAccessTokenValiditySeconds(7200);
        // 刷新令牌默认有效3天 3*24*60*60
        services.setRefreshTokenValiditySeconds(259200);
        return services;
    }

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired  // 配置在SecurityConfig文件中
    private AuthenticationManager authenticationManager;

    // 设置授权码模式的授权码如何存取
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    // 令牌访问端点配置
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)  // 密码模式需要
                .authorizationCodeServices(authorizationCodeServices)  // 授权码模式需要
                // 令牌管理服务
                .tokenServices(tokenServices())
                // 允许post提交
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    // 令牌访问端点安全策略
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")      // 公开/oauth/token_key
                .checkTokenAccess("permitAll()")    // 公开/oauth/check_token
                // 允许表单认证申请令牌
                .allowFormAuthenticationForClients();
    }
}
