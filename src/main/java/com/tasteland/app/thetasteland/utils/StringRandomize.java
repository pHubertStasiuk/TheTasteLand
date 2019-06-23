package com.tasteland.app.thetasteland.utils;

import java.security.SecureRandom;
import java.util.Random;

public class StringRandomize {

    private final static Random random = new SecureRandom();
    private final static String alphabet = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789";
    private final static Integer length = 15;


    public static String generateUserId() {
        return generateRandomString(length);
    }

    public static String generateUserId(int length) {
        return generateRandomString(length);
    }


    private static String generateRandomString(int length) {
        StringBuilder userId = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            userId.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return userId.toString();
    }
}
