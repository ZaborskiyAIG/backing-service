package com.backing.service.util;

import com.backing.service.exception.BalanceException;
import com.backing.service.exception.PinCodeNotEqualsException;

import java.math.BigDecimal;

public final class BankAccountValidation {

    private BankAccountValidation() {
    }

    public static void equalsPinCode(String pinCode, String anotherPinCode) {
        if (!pinCode.equals(anotherPinCode)) {
            throw new PinCodeNotEqualsException("pin-codes not equal");
        }
    }

    public static void checkBalance(BigDecimal balance, BigDecimal price) {
        if (balance.compareTo(price) < 0) {
            throw new BalanceException("there are not enough funds on the balance");
        }
    }
}