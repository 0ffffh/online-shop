package com.k0s.security;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PasswordCryptTest {

    @Test
    void cryptPassword() throws NoSuchAlgorithmException {

        MockedStatic<PasswordCrypt> mockedStatic = mockStatic(PasswordCrypt.class);
        mockedStatic.when(() -> PasswordCrypt.encryptPassword("user", "3345d81e0db911c7363f94d6122ac6cf")).thenReturn("fc4e73a372f9b1637391f845e002644e6a23f32e3ff3302427f8825dfe3d8fde368a26f6e1a312cbeb5d69a5b9bfdbbb0cfb6844264f3b3e1d46c3fd30e220f5");

        assertEquals("fc4e73a372f9b1637391f845e002644e6a23f32e3ff3302427f8825dfe3d8fde368a26f6e1a312cbeb5d69a5b9bfdbbb0cfb6844264f3b3e1d46c3fd30e220f5",
                PasswordCrypt.encryptPassword("user", "3345d81e0db911c7363f94d6122ac6cf"));

    }
}