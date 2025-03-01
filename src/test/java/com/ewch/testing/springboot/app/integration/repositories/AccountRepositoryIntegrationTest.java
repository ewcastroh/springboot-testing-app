package com.ewch.testing.springboot.app.integration.repositories;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.repositories.AccountRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration-jpa-test")
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

    @Test
    void testFindAllEmpty() {
        accountRepository.deleteAll();
        List<Account> accounts = accountRepository.findAll();
        assertAll(
                () -> assertTrue(accounts.isEmpty()),
                () -> assertEquals(0, accounts.size())
        );
    }

    @Test
    void testSave() {
        Account account = new Account(null, "Tim Doe", new BigDecimal(9000));

        Account savedAccount = accountRepository.save(account);

        assertAll(
                () -> assertEquals("Tim Doe", savedAccount.getPerson()),
                () -> assertEquals(9000, savedAccount.getBalance().intValue())
        );
    }

    @Test
    void testUpdate() {
        Account account = new Account(null, "Tim Doe", new BigDecimal(9000));

        Account savedAccount = accountRepository.save(account);

        assertAll(
                () -> assertEquals("Tim Doe", savedAccount.getPerson()),
                () -> assertEquals(9000, savedAccount.getBalance().intValue())
        );

        savedAccount.setPerson("Timothy Doe");
        savedAccount.setBalance(new BigDecimal(9500));

        Account updatedAccount = accountRepository.save(savedAccount);

        assertAll(
                () -> assertEquals("Timothy Doe", updatedAccount.getPerson()),
                () -> assertEquals(9500, updatedAccount.getBalance().intValue())
        );
    }

    @Test
    void testDelete() {
        Account account = new Account(null, "Tim Doe", new BigDecimal(9000));

        Account savedAccount = accountRepository.save(account);

        assertAll(
                () -> assertEquals("Tim Doe", savedAccount.getPerson()),
                () -> assertEquals(9000, savedAccount.getBalance().intValue())
        );

        accountRepository.delete(savedAccount);

        List<Account> accounts = accountRepository.findAll();

        assertAll(
                () -> assertFalse(accounts.isEmpty()),
                () -> assertEquals(5, accounts.size()),
                () -> assertFalse(accounts.contains(savedAccount)),
                () -> assertFalse(accounts.stream().anyMatch(a -> a.getId().equals(savedAccount.getId()))),
                () -> assertThrows(NoSuchElementException.class, () -> accountRepository.findById(savedAccount.getId()).orElseThrow())
        );
    }
}
