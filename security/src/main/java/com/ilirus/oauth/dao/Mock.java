package com.ilirus.oauth.dao;

import com.ilirus.oauth.entities.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class Mock {
    public UserEntity getUser(String access) {
        if("wan".equals(access)){
            return UserEntity.ofAccess(access)
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
