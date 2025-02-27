package com.ewch.testing.springboot.app.services;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.Bank;
import com.ewch.testing.springboot.app.repositories.AccountRepository;
import com.ewch.testing.springboot.app.repositories.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    public List<Account> findAll() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public Account findById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Account not found."));
    }

    @Override
    public Account save(Account account) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public int getTotalTransactions(Long BankId) {
        Bank bank = bankRepository.findById(BankId).orElseThrow(() -> new NoSuchElementException("Bank not found."));
        if (bank != null) {
            return bank.getTotalTransactions();
        }
        return 0;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NoSuchElementException("Account not found."));
        ;
        return account != null ? account.getBalance() : null;
    }

    @Override
    @Transactional
    public void transfer(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow(() -> new NoSuchElementException("Account not found."));
        fromAccount.debit(amount);
        accountRepository.save(fromAccount);

        Account toAccount = accountRepository.findById(toAccountId).orElseThrow(() -> new NoSuchElementException("Account not found."));
        toAccount.credit(amount);
        accountRepository.save(toAccount);

        Bank bank = bankRepository.findById(bankId).orElseThrow(() -> new NoSuchElementException("Bank not found."));
        int totalTransactions = bank.getTotalTransactions();
        bank.setTotalTransactions(++totalTransactions);
        bankRepository.save(bank);
    }
}
