package com.ewch.testing.springboot.app.services;

import com.ewch.testing.springboot.app.models.Account;

import java.math.BigDecimal;

public interface AccountService {

    Account findById(Long id);

    int getTotalTransactions(Long BankId);

    BigDecimal getBalance(Long accountId);

    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount);
}
