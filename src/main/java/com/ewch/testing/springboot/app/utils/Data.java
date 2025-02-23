package com.ewch.testing.springboot.app.utils;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.Bank;

import java.math.BigDecimal;

public class Data {

    public static final Account ACCOUNT_001 = new Account(1L, "John Doe", new BigDecimal("1000"));
    public static final Account ACCOUNT_002 = new Account(2L, "Jane Doe", new BigDecimal("2000"));
    public static final Account ACCOUNT_003 = new Account(3L, "Tom Smith", new BigDecimal("3000"));
    public static final Account ACCOUNT_004 = new Account(4L, "Jenny Smith", new BigDecimal("4000"));
    public static final Account ACCOUNT_005 = new Account(5L, "Emily Curtis", new BigDecimal("5000"));

    public static final Bank BANK_001 = new Bank(1L, "Bank of America", 0);
    public static final Bank BANK_002 = new Bank(2L, "Wells Fargo", 0);
    public static final Bank BANK_003 = new Bank(3L, "Chase", 0);
}
