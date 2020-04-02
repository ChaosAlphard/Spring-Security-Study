package com.ilirus.oauth.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "ofAccess")
@Accessors(chain = true)
public class User {
    @NonNull
    String access;
    String password;
    String name;
    String role;
}
