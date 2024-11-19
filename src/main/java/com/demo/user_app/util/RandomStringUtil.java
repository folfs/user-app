package com.demo.user_app.util;

import java.security.SecureRandom;

public class RandomStringUtil {
    private static final String CHAR_POOL = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGIT_POOL = "0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String randomStr(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(CHAR_POOL.length());
            sb.append(CHAR_POOL.charAt(randomIndex));
        }
        return sb.toString();
    }
    public static String randomNumStr(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(DIGIT_POOL.length());
            sb.append(DIGIT_POOL.charAt(randomIndex));
        }
        return sb.toString();
    }
}
