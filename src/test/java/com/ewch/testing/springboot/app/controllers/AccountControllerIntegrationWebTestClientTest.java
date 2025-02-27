package com.ewch.testing.springboot.app.controllers;

import com.ewch.testing.springboot.app.models.dtos.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIntegrationWebTestClientTest {

    private static final String API_V1_ACCOUNTS = "/api/v1/accounts";
    private static final String API_V1_ACCOUNTS_ID = "/api/v1/accounts/{id}";
    private static final String API_V1_ACCOUNTS_TRANSFER = "/api/v1/accounts/transfer";

    @Autowired
    private WebTestClient webTestClient;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testTransfer() throws JsonProcessingException {
        TransactionDto transactionDto = new TransactionDto(1L, 2L, new BigDecimal("100"), 1L);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer successful.");
        response.put("status", HttpStatus.OK.value());
        response.put("date", LocalDate.now().toString());
        response.put("transaction", transactionDto);

        webTestClient.post()
                .uri(API_V1_ACCOUNTS_TRANSFER)
                .bodyValue(transactionDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(responseEntityResult -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(Objects.requireNonNull(responseEntityResult.getResponseBody()));
                        assertEquals("Transfer successful.", jsonNode.path("message").asText());
                        assertEquals(HttpStatus.OK.value(), jsonNode.path("status").asInt());
                        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
                        assertEquals(transactionDto.fromAccountId(), jsonNode.path("transaction").path("fromAccountId").asLong());
                        assertEquals(transactionDto.toAccountId(), jsonNode.path("transaction").path("toAccountId").asLong());
                        assertEquals(transactionDto.amount(), jsonNode.path("transaction").path("amount").decimalValue());
                        assertEquals(transactionDto.bankId(), jsonNode.path("transaction").path("bankId").asLong());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .jsonPath("$.message").isNotEmpty()
                .jsonPath("$.message").isEqualTo("Transfer successful.")
                .jsonPath("$.message").value(is("Transfer successful."))
                .jsonPath("$.message").value(message -> assertEquals("Transfer successful.", message))
                .jsonPath("$.status").isEqualTo(HttpStatus.OK.value())
                .jsonPath("$.date").isEqualTo(LocalDate.now().toString())
                .jsonPath("$.transaction.fromAccountId").isEqualTo(transactionDto.fromAccountId())
                .jsonPath("$.transaction.toAccountId").isEqualTo(transactionDto.toAccountId())
                .jsonPath("$.transaction.amount").isEqualTo(transactionDto.amount())
                .jsonPath("$.transaction.bankId").isEqualTo(transactionDto.bankId())
                .json(objectMapper.writeValueAsString(response));
    }
}
