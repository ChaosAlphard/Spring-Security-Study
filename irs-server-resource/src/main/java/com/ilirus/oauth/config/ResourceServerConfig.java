package com.ilirus.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final String SIGNING_KEY = "jwtkey";

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("res1")  // 资源id, 需要与ClientDetailsServiceConfigurer中的resourceIds一致
                //.tokenServices(tokenService()) // 验证令牌服务 // 使用JWT后不再需要
                .tokenStore(tokenStore)
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**")
                // 校验scope
                .access("#oauth2.hasScope('all')")
            .and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /* 更换JWT令牌后不需要再远程到授权服务器校验身份
    @Bean
    public ResourceServerTokenServices tokenService() {
        // 远程服务器校验token。调用授权服务器校验token
        RemoteTokenServices services = new RemoteTokenServices();
        // 使用远程服务校验时，必须指定endpoint_url, client_id, client_secret
        services.setCheckTokenEndpointUrl("http://127.0.0.1:80/oauth/check_token");
        services.setClientId("c1");
        services.setClientSecret("secret");
        return services;
    }
    */
}
