package com.ewch.testing.springboot.app.controllers;

import com.ewch.testing.springboot.app.models.Account;
import com.ewch.testing.springboot.app.models.dtos.TransactionDto;
import com.ewch.testing.springboot.app.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Find account by id", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Account.class)
                            )}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content)})
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Account> findById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @Operation(summary = "Transfer money from an account to another one", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Transfer successful.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TransactionDto.class)
                            )}),
            @ApiResponse(responseCode = "400", description = "Invalid transaction data supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content)})
    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> transfer(@RequestBody TransactionDto transactionDto) {
        accountService.transfer(
                transactionDto.fromAccountId(),
                transactionDto.toAccountId(),
                transactionDto.amount(),
                transactionDto.bankId());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Transfer successful.");
        response.put("status", HttpStatus.OK.value());
        response.put("date", LocalDateTime.now());
        response.put("transaction", transactionDto);

        return ResponseEntity.ok(response);
    }
}
