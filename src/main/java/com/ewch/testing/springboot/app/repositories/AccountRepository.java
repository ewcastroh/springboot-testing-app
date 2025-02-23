package com.ewch.testing.springboot.app.repositories;

import com.ewch.testing.springboot.app.models.Account;

import java.util.List;

public interface AccountRepository {

    List<Account> findAll();

    Account findById(Long id);

    void update(Account account);
}
