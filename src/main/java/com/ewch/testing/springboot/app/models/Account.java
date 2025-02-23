package com.ewch.testing.springboot.app.models;

import com.ewch.testing.springboot.app.exceptions.NotEnoughMoneyException;

import java.math.BigDecimal;
import java.util.Objects;

public class Account {
    private Long id;
    private String person;
    private BigDecimal balance;

    public Account() {
    }

    public Account(Long id, String person, BigDecimal balance) {
        this.id = id;
        this.person = person;
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return Objects.equals(id, account.id) && Objects.equals(person, account.person) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, person, balance);
    }

    public void debit(BigDecimal amount) {
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException("There is not enough money in the account.");
        }
        balance = newBalance;
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
