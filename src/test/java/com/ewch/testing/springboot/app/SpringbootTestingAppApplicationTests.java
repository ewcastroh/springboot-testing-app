package com.ewch.testing.springboot.app;

import com.ewch.testing.springboot.app.exceptions.NotEnoughMoneyException;
import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.Bank;
import com.ewch.testing.springboot.app.repositories.AccountRepository;
import com.ewch.testing.springboot.app.repositories.BankRepository;
import com.ewch.testing.springboot.app.services.AccountService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringbootTestingAppApplicationTests {

    @MockitoBean
    AccountRepository accountRepository;

    @MockitoBean
    BankRepository bankRepository;

    @Autowired
    AccountService accountService;

    Account account001;
    Account account002;
    Bank bank001;
    List<Account> accounts;

    @BeforeEach
    void setUp() throws IOException {
        // accountRepository = mock(AccountRepository.class);
        // bankRepository = mock(BankRepository.class);
        // accountService = new AccountServiceImpl(accountRepository, bankRepository);

        account001 = new Account(1L, "John Doe", new BigDecimal("1000"));
        account002 = new Account(2L, "Jane Doe", new BigDecimal("2000"));
        bank001 = new Bank(1L, "Bank of America", 0);

        ObjectMapper mapper = new ObjectMapper();
        String accountsJsonPath = "src/test/resources/accounts.json";
        accounts = mapper.readValue(Paths.get(accountsJsonPath).toFile(), new TypeReference<>() {
        });
    }

    @Test
    void testTransferUpdatesBalancesAndTransactions() {
        when(accountRepository.findById(1L)).thenReturn(account001);
        when(accountRepository.findById(2L)).thenReturn(account002);
        when(bankRepository.findById(1L)).thenReturn(bank001);

        accountService.transfer(1L, 2L, new BigDecimal("100"), 1L);

        BigDecimal fromAccountBalance = accountService.getBalance(1L);
        BigDecimal toAccountBalance = accountService.getBalance(2L);
        int totalTransactions = accountService.getTotalTransactions(1L);

        assertEquals(new BigDecimal("900"), fromAccountBalance);
        assertEquals(new BigDecimal("2100"), toAccountBalance);
        assertEquals(1, totalTransactions);

        verify(accountRepository, times(2)).findById(1L);
        verify(accountRepository, times(2)).findById(2L);
        verify(accountRepository, times(2)).update(any(Account.class));
        verify(bankRepository, times(2)).findById(1L);
        verify(bankRepository).update(any(Bank.class));
        verify(accountRepository, never()).findAll();
    }

    @Test
    void testTransferThrowsExceptionWhenInsufficientFunds() {
        when(accountRepository.findById(1L)).thenReturn(account001);
        when(accountRepository.findById(2L)).thenReturn(account002);
        when(bankRepository.findById(1L)).thenReturn(bank001);

        assertThrows(NotEnoughMoneyException.class, () ->
                accountService.transfer(1L, 2L, new BigDecimal("1200"), 1L)
        );

        BigDecimal fromAccountBalance = accountService.getBalance(1L);
        BigDecimal toAccountBalance = accountService.getBalance(2L);
        int totalTransactions = accountService.getTotalTransactions(1L);

        assertEquals(new BigDecimal("1000"), fromAccountBalance);
        assertEquals(new BigDecimal("2000"), toAccountBalance);
        assertEquals(0, totalTransactions);

        verify(accountRepository, times(2)).findById(1L);
        verify(accountRepository, times(1)).findById(2L);
        verify(accountRepository, never()).update(any(Account.class));
        verify(bankRepository, times(1)).findById(1L);
        verify(bankRepository, never()).update(any(Bank.class));
        verify(accountRepository, never()).findAll();
    }

    @Test
    void testFindById() {
        when(accountRepository.findById(1L)).thenReturn(account001);

        Account account1 = accountService.findById(1L);
        Account account2 = accountService.findById(1L);

        assertNotNull(account1);
        assertNotNull(account2);
        assertSame(account1, account2);
        assertTrue(account1 == account2);
        assertEquals("John Doe", account1.getPerson());
        assertEquals(new BigDecimal("1000"), account1.getBalance());

        verify(accountRepository, times(2)).findById(1L);
        verify(accountRepository, never()).findAll();
    }
}
