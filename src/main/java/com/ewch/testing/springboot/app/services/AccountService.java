package com.ewch.testing.springboot.app.services;

import com.ewch.testing.springboot.app.models.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    List<Account> findAll();

    Account findById(Long id);

    Account save(Account account);

    int getTotalTransactions(Long BankId);

    BigDecimal getBalance(Long accountId);

    void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId);
}
