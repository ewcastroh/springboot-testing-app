package com.ewch.testing.springboot.app.repositories;

import com.ewch.testing.springboot.app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Methods not required because JpaRepository provides them.
    // List<Account> findAll();
    // Account findById(Long id);
    // void update(Account account);

    @Query("SELECT a FROM Account a WHERE a.person = ?1")
    Optional<Account> findByPerson(String person);
}
