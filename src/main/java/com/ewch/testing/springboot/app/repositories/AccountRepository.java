package com.ewch.testing.springboot.app.repositories;

import com.ewch.testing.springboot.app.models.Account;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository {

    List<Account> findAll();

    Account findById(Long id);

    void update(Account account);
}
