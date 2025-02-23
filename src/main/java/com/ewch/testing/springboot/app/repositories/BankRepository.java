package com.ewch.testing.springboot.app.repositories;

import com.ewch.testing.springboot.app.models.Bank;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRepository {

    List<Bank> findAll();

    Bank findById(Long id);

    void update(Bank bank);
}
