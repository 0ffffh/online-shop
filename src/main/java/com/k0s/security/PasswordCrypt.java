package com.k0s.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class PasswordCrypt {
    private static final String SALT = "It is a period of civil war.\n" +
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


    public static String encryptPassword(String password, String userSalt){
        System.out.println("password = " + password);
        System.out.println("random salt = " + userSalt);
        System.out.println("static salt = " + SALT);

        String generatedPassword = null;
        try {
            //TODO: вынести в отдельный метод генерацию хэша!!! 030.Au 18 1:02
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(SALT.getBytes(StandardCharsets.UTF_8));
            md.update(userSalt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            generatedPassword = HexFormat.of().formatHex(bytes);
            System.out.println("generated pasword = " + generatedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
