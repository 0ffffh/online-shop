package com.k0s.entity.user;

import java.util.StringJoiner;

public class User {
    private long id;
    private String name;
    private String password;
    private String salt;
    private Role role;

    public User() {
    }

    public User(long id, String name, String password, String salt, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("password='" + password + "'")
                .add("salt='" + salt + "'")
                .add("role=" + role)
                .toString();
    }
}
