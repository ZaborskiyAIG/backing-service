package com.backing.service.util;

import java.util.Random;

public final class AccountNumberUtil {

    private AccountNumberUtil() {
    }

    public static String generatedAccountNumber() {
        int countNumber = 16;
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < countNumber; i++) {
            stringBuilder.append(random.nextInt(10));
        }

        return stringBuilder.toString();
    }
}