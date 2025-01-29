package com.transaction_service.controller;

import com.transaction_service.dto.TransactionRequest;
import com.transaction_service.dto.TransactionResponse;
import com.transaction_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions Controller", description = "Endpoints for transactions management")
@SecurityRequirement(name = "BearerAuth")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(description = "Transfer to other accounts.")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransactionRequest request) {
        log.info("Iniciando tranferencia de fondos");
        TransactionResponse response = transactionService.transfer(request);
        log.info("Tranferencia exitosa!");
        return ResponseEntity.ok(response);
    }

    @Operation(description = "Deposit account")
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody TransactionRequest request) {
        log.info("Iniciando deposito a cuenta");
        TransactionResponse response = transactionService.deposit(request);
        log.info("Deposito exitoso");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
        log.info("Iniciando retiro de la cuenta");
        TransactionResponse response = transactionService.withdraw(request);
        log.info("Retiro exitoso");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(@PathVariable Long accountId) {
        log.info("Consultando transacciones de la cuenta");
        List<TransactionResponse> responses = transactionService.getTransactionHistory(accountId);
        log.info("Devolviendo transacciones de la cuenta");
        return ResponseEntity.ok(responses);
    }
}
