package com.k0s.security;

import com.k0s.security.user.User;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class PasswordCryptTest {

    @Test
    void cryptPassword() {

        User user = User.builder()
                .password("fc4e73a372f9b1637391f845e002644e6a23f32e3ff3302427f8825dfe3d8fde368a26f6e1a312cbeb5d69a5b9bfdbbb0cfb6844264f3b3e1d46c3fd30e220f5")
                .salt("3345d81e0db911c7363f94d6122ac6cf")
                .build();

        assertEquals(user.getPassword(), PasswordCrypt.encryptPassword("user", user.getSalt()));
        assertNotEquals(user.getPassword(), PasswordCrypt.encryptPassword("USER", user.getSalt()));

    }
}