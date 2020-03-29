package com.ilirus.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*@Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("user")
                .password("123")
                .roles("USER")
                .build());

        return manager;
    }*/

    // 密码编码设置
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 在内存中创建用户
        auth.inMemoryAuthentication()
            // 使用BCryptPasswordEncoder 加密密码
                .passwordEncoder(new BCryptPasswordEncoder())
            // 用户名为 user
                .withUser("user")
            // 密码为 123
                .password(new BCryptPasswordEncoder().encode("123"))
            //拥有 USER 权限
                .roles("USER");
    }*/

    // 安全拦截机制
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            // 匹配"/","/index","/error"
                .antMatchers("/","/index","/error")
            // 不需要权限即可访问
                .permitAll()
            // 匹配"/user" 路径下的所有
                .antMatchers("/user/**")
            // 拥有USER 权限才可访问
                .hasRole("USER")
                .antMatchers("/admin/**")
                .hasRole("ADMIN")
                .and()
            // 允许使用表单登录
                .formLogin()
            // 登录地址为 "/login"
                .loginPage("/login")
            // 指定处理登录的url, 即登录表单的action对应的地址
                .loginProcessingUrl("/login/identify")
            // 成功后默认跳转到"/user"
                .defaultSuccessUrl("/")
                .and().logout()
            // 注销地址为 "logout"
                .logoutUrl("/logout")
            // 注销后跳转到 "/login"
                .logoutSuccessUrl("/login")
            // 注销时是否让HttpSession 无效化
                .invalidateHttpSession(true)
                .and()
            // 禁用跨域请求验证
                .csrf().disable();
    }
}
