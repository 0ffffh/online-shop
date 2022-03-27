package com.k0s.entity.user;

import lombok.*;


@AllArgsConstructor
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
