package com.ewch.testing.springboot.app.repositories;

import com.ewch.testing.springboot.app.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    // Methods not required because JpaRepository provides them.
    // List<Bank> findAll();
    // Bank findById(Long id);
    // void update(Bank bank);
}
