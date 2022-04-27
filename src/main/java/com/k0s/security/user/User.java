package com.k0s.security.user;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class User {
    private long id;
    private String name;
    private String password;
    private String salt;
    private Role role;
}
