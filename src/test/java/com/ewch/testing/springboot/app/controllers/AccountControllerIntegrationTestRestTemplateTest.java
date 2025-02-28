package com.ewch.testing.springboot.app.controllers;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.dtos.TransactionDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerIntegrationTestRestTemplateTest {

    private static final String API_V1_ACCOUNTS = "/api/v1/accounts";
    private static final String API_V1_ACCOUNTS_ID = "/api/v1/accounts/";
    private static final String API_V1_ACCOUNTS_TRANSFER = "/api/v1/accounts/transfer";

    @Autowired
    private TestRestTemplate testRestTemplate;

    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void testTransfer() throws JsonProcessingException {
        System.out.println("port = " + port);
        TransactionDto transactionDto = new TransactionDto(1L, 2L, new BigDecimal(100), 1L);

        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("message", "Transfer successful.");
        expectedResponse.put("status", HttpStatus.OK.value());
        expectedResponse.put("date", LocalDate.now().toString());
        expectedResponse.put("transaction", transactionDto);

        ResponseEntity<String> stringResponseEntity = testRestTemplate.postForEntity(API_V1_ACCOUNTS_TRANSFER, transactionDto, String.class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        String jsonBodyResponse = stringResponseEntity.getBody();
        System.out.println("body = " + jsonBodyResponse);

        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, stringResponseEntity.getHeaders().getContentType());
        assertNotNull(jsonBodyResponse);
        assertTrue(jsonBodyResponse.contains("Transfer successful."));

        JsonNode jsonNode = objectMapper.readTree(jsonBodyResponse);
        assertNotNull(jsonNode);
        assertEquals("Transfer successful.", jsonNode.path("message").asText());
        assertEquals(LocalDate.now().toString(), jsonNode.path("date").asText());
        assertEquals("100", jsonNode.path("transaction").path("amount").asText());
        assertEquals(1L, jsonNode.path("transaction").path("fromAccountId").asLong());
        assertEquals(2L, jsonNode.path("transaction").path("toAccountId").asLong());
        assertEquals(objectMapper.writeValueAsString(expectedResponse), jsonBodyResponse);
    }

    @Test
    @Order(2)
    void testFindById() {
        ResponseEntity<Account> stringResponseEntity = testRestTemplate.getForEntity(API_V1_ACCOUNTS_ID + "1", Account.class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        Account response = stringResponseEntity.getBody();
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, stringResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John Doe", response.getPerson());
        assertEquals("1000.00", response.getBalance().toPlainString());
    }
}