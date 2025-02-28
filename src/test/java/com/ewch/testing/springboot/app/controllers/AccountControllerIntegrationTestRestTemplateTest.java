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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        assertEquals("900.00", response.getBalance().toPlainString());
    }

    @Test
    @Order(3)
    void testFindAllUsingArray() {
        ResponseEntity<Account[]> stringResponseEntity = testRestTemplate.getForEntity(API_V1_ACCOUNTS, Account[].class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        Account[] response = stringResponseEntity.getBody();
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, stringResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(5, response.length);
        assertEquals(1L, response[0].getId());
        assertEquals("John Doe", response[0].getPerson());
        assertEquals("900.00", response[0].getBalance().toPlainString());
        assertEquals(2L, response[1].getId());
        assertEquals("Jane Doe", response[1].getPerson());
        assertEquals("2100.00", response[1].getBalance().toPlainString());
    }

    @Test
    @Order(3)
    void testFindAllUsingList() {
        ResponseEntity<Account[]> stringResponseEntity = testRestTemplate.getForEntity(API_V1_ACCOUNTS, Account[].class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        List<Account> response = Arrays.asList(Objects.requireNonNull(stringResponseEntity.getBody()));
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, stringResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, stringResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(5, response.size());
        assertEquals(1L, response.getFirst().getId());
        assertEquals("John Doe", response.getFirst().getPerson());
        assertEquals("900.00", response.getFirst().getBalance().toPlainString());
        assertEquals(2L, response.get(1).getId());
        assertEquals("Jane Doe", response.get(1).getPerson());
        assertEquals("2100.00", response.get(1).getBalance().toPlainString());

        JsonNode jsonNode = objectMapper.valueToTree(response);
        assertNotNull(jsonNode);
        assertEquals(5, jsonNode.size());
        assertEquals(1L, jsonNode.get(0).path("id").asLong());
        assertEquals("John Doe", jsonNode.get(0).path("person").asText());
        assertEquals(900.0, jsonNode.get(0).path("balance").asDouble());
        assertEquals(2L, jsonNode.get(1).path("id").asLong());
        assertEquals("Jane Doe", jsonNode.get(1).path("person").asText());
        assertEquals(2100.0, jsonNode.get(1).path("balance").asDouble());
    }

    @Test
    @Order(4)
    void testSave() {
        Account account = new Account(null, "Tim Timburton", new BigDecimal(3000));

        ResponseEntity<Account> stringResponseEntity = testRestTemplate.postForEntity(API_V1_ACCOUNTS, account, Account.class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        Account response = stringResponseEntity.getBody();
        System.out.println("body = " + response);

        assertEquals(HttpStatus.CREATED, stringResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, stringResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(6L, response.getId());
        assertEquals("Tim Timburton", response.getPerson());
        assertEquals("3000", response.getBalance().toPlainString());
    }

    @Test
    @Order(5)
    void testDeleteById() {
        ResponseEntity<Account[]> allAccountsResponseEntity  = testRestTemplate.getForEntity(API_V1_ACCOUNTS, Account[].class);
        System.out.println("stringResponseEntity = " + allAccountsResponseEntity);

        List<Account> response = Arrays.asList(Objects.requireNonNull(allAccountsResponseEntity.getBody()));
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, allAccountsResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, allAccountsResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(6, response.size());

        testRestTemplate.delete(API_V1_ACCOUNTS_ID + "6");

        allAccountsResponseEntity  = testRestTemplate.getForEntity(API_V1_ACCOUNTS, Account[].class);
        System.out.println("stringResponseEntity = " + allAccountsResponseEntity);

        response = Arrays.asList(Objects.requireNonNull(allAccountsResponseEntity.getBody()));
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, allAccountsResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, allAccountsResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(5, response.size());

        ResponseEntity<Void> stringResponseEntity = testRestTemplate.getForEntity(API_V1_ACCOUNTS_ID + "6", Void.class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        assertEquals(HttpStatus.NOT_FOUND, stringResponseEntity.getStatusCode());
        assertNull(stringResponseEntity.getBody());
    }

    @Test
    @Order(6)
    void testDeleteByIdUsingExchange() {
        ResponseEntity<Account[]> allAccountsResponseEntity  = testRestTemplate.getForEntity(API_V1_ACCOUNTS, Account[].class);
        System.out.println("stringResponseEntity = " + allAccountsResponseEntity);

        List<Account> response = Arrays.asList(Objects.requireNonNull(allAccountsResponseEntity.getBody()));
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, allAccountsResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, allAccountsResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(5, response.size());

        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id", 3L);
        ResponseEntity<Void> exchange = testRestTemplate
                .exchange(API_V1_ACCOUNTS_ID + "{id}", HttpMethod.DELETE, null, Void.class, pathVariables);
        System.out.println("exchange = " + exchange);
        assertEquals(HttpStatus.NO_CONTENT, exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        allAccountsResponseEntity  = testRestTemplate.getForEntity(API_V1_ACCOUNTS, Account[].class);
        System.out.println("stringResponseEntity = " + allAccountsResponseEntity);

        response = Arrays.asList(Objects.requireNonNull(allAccountsResponseEntity.getBody()));
        System.out.println("body = " + response);

        assertEquals(HttpStatus.OK, allAccountsResponseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, allAccountsResponseEntity.getHeaders().getContentType());
        assertNotNull(response);
        assertEquals(4, response.size());

        ResponseEntity<Void> stringResponseEntity = testRestTemplate.getForEntity(API_V1_ACCOUNTS_ID + "6", Void.class);
        System.out.println("stringResponseEntity = " + stringResponseEntity);

        assertEquals(HttpStatus.NOT_FOUND, stringResponseEntity.getStatusCode());
        assertNull(stringResponseEntity.getBody());
    }
}
