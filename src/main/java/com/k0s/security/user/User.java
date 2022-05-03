package com.k0s.security.user;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private long id;
    private String name;
    private String password;
    private String salt;
    private Role role;

    public void setRole(String role) {
        this.role = Role.getRole(role);
    }
}
