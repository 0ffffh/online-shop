package com.k0s.dao;

import com.k0s.entity.user.User;

public interface UserDao {

    User getUser(String name);
}
