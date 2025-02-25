package com.ewch.testing.springboot.app.models.dtos;

import java.math.BigDecimal;

public record TransactionDto(Long fromAccountId, Long toAccountId, BigDecimal amount, Long bankId) {
}
