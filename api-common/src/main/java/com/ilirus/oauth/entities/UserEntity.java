package com.ilirus.oauth.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "ofAccess")
@Accessors(chain = true)
public class UserEntity {
    @NonNull
    String access;
    String password;
    String name;
    String role;

    public List<SimpleGrantedAuthority> getRole() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(role.split(","))
                .forEach(role -> authorities
                        .add(new SimpleGrantedAuthority("ROLE_" + role)));
        return authorities;
    }
}
