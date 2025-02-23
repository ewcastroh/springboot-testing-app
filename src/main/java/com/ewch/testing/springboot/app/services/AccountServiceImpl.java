package com.ewch.testing.springboot.app.services;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.Bank;
import com.ewch.testing.springboot.app.repositories.AccountRepository;
import com.ewch.testing.springboot.app.repositories.BankRepository;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;
    private BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public int getTotalTransactions(Long BankId) {
        Bank bank = bankRepository.findById(BankId);
        if (bank != null) {
            return bank.getTotalTransactions();
        }
        return 0;
    }

    @Override
    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId);
        return account != null ? account.getBalance() : null;
    }

    @Override
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId) {
        Bank bank = bankRepository.findById(bankId);
        int totalTransactions = bank.getTotalTransactions();
        bank.setTotalTransactions(++totalTransactions);
        bankRepository.update(bank);

        Account fromAccount = accountRepository.findById(fromAccountId);
        fromAccount.debit(amount);
        accountRepository.update(fromAccount);

        Account toAccount = accountRepository.findById(toAccountId);
        toAccount.credit(amount);
        accountRepository.update(toAccount);
    }
}
