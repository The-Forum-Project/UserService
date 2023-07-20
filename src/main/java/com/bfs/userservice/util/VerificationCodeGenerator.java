package com.bfs.userservice.util;

import java.util.Random;


public class VerificationCodeGenerator {
    private static final String DIGITS = "0123456789";

    public static String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(DIGITS.length());
            char digit = DIGITS.charAt(index);
            code.append(digit);
        }

        return code.toString();
    }
}

