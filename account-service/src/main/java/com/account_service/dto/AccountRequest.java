package com.account_service.dto;

import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {

    @NotNull
    private AccountType accountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal balance;

    @NotNull
    private Long clientId;
}

