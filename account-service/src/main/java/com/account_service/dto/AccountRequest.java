package com.account_service.dto;

import javax.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountRequest {

    @NotNull
    private AccountType accountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal balance;

    @NotNull
    private Long clientId;
}

