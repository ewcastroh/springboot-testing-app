package com.ewch.testing.springboot.app.controllers;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.dtos.TransactionDto;
import com.ewch.testing.springboot.app.services.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testFindById() throws Exception {
        when(accountService.findById(1L)).thenReturn(new Account(1L, "John Doe", new BigDecimal("100.0")));

        mockMvc.perform(get("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.person").value("John Doe"))
                .andExpect(jsonPath("$.balance").value(100.0));
        verify(accountService).findById(1L);
    }

    @Test
    void testTransfer() throws Exception {
        Account fromAccount = new Account(1L, "John Doe", new BigDecimal("100.0"));
        Account toAccount = new Account(2L, "Jane Doe", new BigDecimal("200.0"));

        TransactionDto transactionDto = new TransactionDto(
                fromAccount.getId(),
                toAccount.getId(),
                new BigDecimal("50.0"),
                1L);

        System.out.println("objectMapper = " + objectMapper.writeValueAsString(transactionDto));

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Transfer successful.");
        expectedResponse.put("status", HttpStatus.OK.value());
        expectedResponse.put("date", LocalDate.now().toString());
        expectedResponse.put("transaction", transactionDto);

        System.out.println("objectMapper = " + objectMapper.writeValueAsString(expectedResponse));

        mockMvc.perform(post("/api/v1/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transfer successful."))
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.date").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.transaction.fromAccountId").value(transactionDto.fromAccountId()))
                .andExpect(jsonPath("$.transaction.toAccountId").value(transactionDto.toAccountId()))
                .andExpect(jsonPath("$.transaction.amount").value(transactionDto.amount()))
                .andExpect(jsonPath("$.transaction.bankId").value(transactionDto.bankId()))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

        verify(accountService).transfer(1L, 2L, new BigDecimal("50.0"), 1L);
    }

    @Test
    void testFindAll() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String accountsJsonPath = "src/test/resources/accounts.json";
        List<Account> accounts = mapper.readValue(Paths.get(accountsJsonPath).toFile(), new TypeReference<>() {
        });

        when(accountService.findAll()).thenReturn(accounts);
        mockMvc.perform(get("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].person").value("John Doe"))
                .andExpect(jsonPath("$[0].balance").value(1000.0))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].person").value("Jane Doe"))
                .andExpect(jsonPath("$[1].balance").value(2000.0))
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));
        verify(accountService).findAll();
    }

    @Test
    void testSave() throws Exception {
        Account account = new Account(null, "Bill Thomson", new BigDecimal("9000.0"));
        when(accountService.save(account)).then(invocation -> {
            Account accountToSave = invocation.getArgument(0);
            accountToSave.setId(9L);
            return accountToSave;
        });

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(9)))
                .andExpect(jsonPath("$.person", is("Bill Thomson")))
                .andExpect(jsonPath("$.balance", is(9000.0)));
        verify(accountService).save(any(Account.class));
    }
}