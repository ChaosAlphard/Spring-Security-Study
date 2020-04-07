package com.ilirus.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        super.configure(security);
    }

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
                .redirectUris("http://www.baidu.com");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        super.configure(endpoints);
    }
}
