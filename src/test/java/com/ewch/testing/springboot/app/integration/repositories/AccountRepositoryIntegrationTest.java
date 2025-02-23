package com.ewch.testing.springboot.app.integration.repositories;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AccountRepositoryIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void testFindById() {
        Optional<Account> account = accountRepository.findById(1L);
        assertAll(
            () -> assertTrue(account.isPresent()),
            () -> assertEquals("John Doe", account.orElseThrow().getPerson())
        );
    }

    @Test
    void testFindByPerson() {
        Optional<Account> account = accountRepository.findByPerson("John Doe");
        assertAll(
            () -> assertTrue(account.isPresent()),
            () -> assertEquals("John Doe", account.orElseThrow().getPerson()),
            () -> assertEquals(1L, account.orElseThrow().getId()),
            () -> assertEquals(1000, account.orElseThrow().getBalance().intValue())
        );
    }

    @Test
    void testFindByPersonThrowsException() {
        Optional<Account> account = accountRepository.findByPerson("Johnny Doe");
        assertAll(
            () -> assertFalse(account.isPresent()),
            () -> assertThrows(NoSuchElementException.class, account::orElseThrow)
        );
    }

    @Test
    void testFindAll() {
        List<Account> accounts = accountRepository.findAll();
        assertAll(
            () -> assertFalse(accounts.isEmpty()),
            () -> assertEquals(5, accounts.size())
        );
    }
}
