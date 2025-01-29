package com.transaction_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionResponse {
    private Long id;
    private Long accountOriginId;
    private Long accountDestinationId;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionDate;
}

