package com.ilirus.oauth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    class Mock {
        public com.ilirus.oauth.entities.User getUser(String access) {
            if("wan".equals(access)){
                return com.ilirus.oauth.entities.User.ofAccess(access)
                        .setName("Ilirus")
                        .setPassword(new BCryptPasswordEncoder().encode("123"))
                        .setRole("USER");
            }
            return null;
        }

        public List<String> getUserRoles(String access) {
            List<String> roles = new ArrayList<>(2);
            if("wan".equals(access)) {
                roles.add("USER");
                roles.add("ADMIN");
            }
            return roles;
        }
    }
}
