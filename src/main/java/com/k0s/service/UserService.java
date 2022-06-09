package com.k0s.service;

import com.k0s.dao.UserDao;
import com.k0s.security.user.Role;
import com.k0s.security.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAll() {
        List<User> userList = userDao.getAll();
        userList.forEach(user -> user.setRoles(new HashSet<>(getUserRolesByUsername(user.getUsername()))));
        return userList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userDao.findByUsername(username);
        if (user.isEmpty()) {
            log.info("User {} not found in database", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        log.info("User {} found in database", username);
        user.get().setRoles(getUserRolesByUsername(username));
        return user.get();
    }

    public List<Role> getUserRolesByUsername(String username) {
        return userDao.getUserRolesByUsername(username);
    }
}




