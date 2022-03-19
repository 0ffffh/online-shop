package com.k0s.security;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

class PasswordCryptTest {

    @Test
    void cryptPassword() throws NoSuchAlgorithmException {
        String SALT = "It is a period of civil war.\n" +
                "Rebel spaceships, striking\n" +
                "from a hidden base, have won\n" +
                "their first victory against\n" +
                "the evil Galactic Empire.\n" +
                "\n" +
                "During the battle, Rebel\n" +
                "spies managed to steal secret\n" +
                "plans to the Empire's\n" +
                "ultimate weapon, the DEATH\n" +
                "STAR, an armored space\n" +
                "station with enough power to\n" +
                "destroy an entire planet.\n" +
                "\n" +
                "Pursued by the Empire's\n" +
                "sinister agents, Princess\n" +
                "Leia races home aboard her\n" +
                "starship, custodian of the\n" +
                "stolen plans that can save\n" +
                "her people and restore\n" +
                "freedom to the galaxy....";

        PasswordCrypt.encryptPassword("user", "3345d81e0db911c7363f94d6122ac6cf");
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomeSalt = new byte[16];
        secureRandom.nextBytes(randomeSalt);
        String hashSalt = HexFormat.of().formatHex(randomeSalt);
        System.out.println("ROOT SALT = " + hashSalt);

        String password  = "user";


        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(SALT.getBytes(StandardCharsets.UTF_8));
        md.update(randomeSalt);


        byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String myHash = HexFormat.of().formatHex(bytes);

        System.out.println("ROOT PASSWORD =  " + myHash);


    }
}