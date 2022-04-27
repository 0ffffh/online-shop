package com.k0s.dao;

import com.k0s.security.user.User;

public interface UserDao {
    User get(String name);
}
