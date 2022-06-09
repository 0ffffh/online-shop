package com.k0s.dao;

import com.k0s.security.user.Role;
import com.k0s.security.user.User;

import java.util.List;
import java.util.Optional;

//@Repository
public interface UserDao {
    Optional<User> findByUsername(String username);
    List<User> getAll();

    List<Role> getUserRolesByUsername(String username);

}
