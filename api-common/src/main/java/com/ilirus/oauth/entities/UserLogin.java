package com.ilirus.oauth.entities;

import lombok.Data;

@Data
public class UserLogin {
    private String username;
    private String password;
    private Boolean remember;
}
