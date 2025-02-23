package com.ewch.testing.springboot.app;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.Bank;
import com.ewch.testing.springboot.app.repositories.AccountRepository;
import com.ewch.testing.springboot.app.repositories.BankRepository;
import com.ewch.testing.springboot.app.services.AccountService;
import com.ewch.testing.springboot.app.services.AccountServiceImpl;
import com.ewch.testing.springboot.app.utils.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class SpringbootTestingAppApplicationTests {

	AccountRepository accountRepository;
	BankRepository bankRepository;
	AccountService accountService;

	@BeforeEach
	void setUp() {
		accountRepository = mock(AccountRepository.class);
		bankRepository = mock(BankRepository.class);
		accountService = new AccountServiceImpl(accountRepository, bankRepository);
	}

	@Test
	void contextLoads() {
		when(accountRepository.findById(1L)).thenReturn(Data.ACCOUNT_001);
		when(accountRepository.findById(2L)).thenReturn(Data.ACCOUNT_002);
		when(bankRepository.findById(1L)).thenReturn(Data.BANK_001);

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
	}
}
