package com.ilirus.oauth.service;

import com.ilirus.oauth.dao.Mock;
import org.springframework.security.core.userdetails.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    // Mock 模拟数据库查询操作
    private Mock dao = new Mock();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 数据库查询用户信息
        com.ilirus.oauth.entities.User user = dao.getUser(username);
        if(user == null) {
            // 查不到用户返回null 由provider 来抛出异常
            return null;
        }
        // 根据用户id 查询用户权限
        List<String> userRoles = dao.getUserRoles(user.getAccess());
        // list 转 数组
        String[] roles = userRoles.toArray(new String[0]);
        return User.withUsername(user.getName())
                .password(user.getPassword()).roles(roles).build();
    }
}
