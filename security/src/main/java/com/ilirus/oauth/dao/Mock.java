package com.ilirus.oauth.dao;

import com.ilirus.oauth.entities.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Mock {
    public User getUser(String access) {
        if("wan".equals(access)){
            User user = new User();
            user.setName("Ilirus");
            user.setAccess("wan");
            user.setPassword(new BCryptPasswordEncoder().encode("123"));
            return user;
        }
        return null;
    }
}
