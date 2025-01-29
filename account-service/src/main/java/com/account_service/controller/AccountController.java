package com.account_service.controller;


import com.account_service.dto.AccountRequest;
import com.account_service.dto.AccountResponse;
import com.account_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Controller", description = "Endpoints for managing bank accounts")
@SecurityRequirement(name = "BearerAuth")
@Slf4j
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Create a new account", description = "Creates a new bank account for a specific client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
        log.info("Creando cuenta..");
        AccountResponse accountResponse = accountService.createAccount(accountRequest);
        log.info("La cuenta se creo correctamente");
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PutMapping("/deposit/{clientId}")
    public ResponseEntity<AccountResponse> depositAccount(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        log.info("Depositando a cuenta..");
        AccountResponse accountResponse = accountService.depositAccount(accountId, amount);
        log.info("El deposito se realizo correctamente");
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PutMapping("/withdraw/{clientId}")
    public ResponseEntity<AccountResponse> withdrawAccount(@PathVariable Long clientId, @RequestParam Long accountId, @RequestParam BigDecimal amount) {
        log.info("Retirando de cuenta..");
        AccountResponse accountResponse = accountService.withdrawAccount(clientId, accountId, amount);
        log.info("El retiro se realizo correctamente");
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @PutMapping("/transfer/{clientId}")
    public ResponseEntity<AccountResponse> transferAccount(@PathVariable Long clientId, @RequestParam Long accountId, @RequestParam Long accountDestinationId, @RequestParam BigDecimal amount) {
        log.info("Realizando tranferencia de cuenta..");
        AccountResponse accountResponse = accountService.transferAccount(clientId, accountId, accountDestinationId, amount);
        log.info("La tranferencia se realizo correctamente");
        return new ResponseEntity<>(accountResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get account by ID", description = "Fetches account details by account ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId) {
        log.info("Consultando cuenta con id : {}", accountId);
        AccountResponse accountResponse = accountService.getAccountById(accountId);
        log.info("Retornando informacion..");
        return ResponseEntity.ok(accountResponse);
    }

    @Operation(summary = "Get all accounts for a client", description = "Retrieves all accounts associated with a specific client.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountResponse>> getAccountsByClientId(@PathVariable Long clientId) {
        log.info("Consultando cuentas del cliente con id: {}", clientId);
        List<AccountResponse> accounts = accountService.getAccountsByClientId(clientId);
        log.info("Retornando cuentas");
        return ResponseEntity.ok(accounts);
    }
}
