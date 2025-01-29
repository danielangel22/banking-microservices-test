package com.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TransactionRequest {
    @NotNull
    private Long clientId;
    @Positive
    private BigDecimal amount;
    @NotNull
    private Long accountOriginId;
    private Long accountDestinationId;
    @NotNull
    private TransactionType transactionType;

    public TransactionRequest() {

    }
}
