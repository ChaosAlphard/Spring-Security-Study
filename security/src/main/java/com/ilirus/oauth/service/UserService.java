package com.ilirus.oauth.service;

import com.ilirus.oauth.dao.Mock;
import org.springframework.security.core.userdetails.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private Mock dao = new Mock();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Username: "+username);
        // 数据库查询
        com.ilirus.oauth.entities.User user = dao.getUser("wan");
        UserDetails userDetails = User.withUsername(user.getName())
                .password(user.getPassword()).roles("USER").build();
        return userDetails;
    }
}
