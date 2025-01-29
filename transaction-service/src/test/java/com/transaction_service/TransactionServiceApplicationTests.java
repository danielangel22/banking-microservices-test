package com.transaction_service;

import com.transaction_service.client.AccountClient;
import com.transaction_service.dto.TransactionRequest;
import com.transaction_service.dto.TransactionResponse;
import com.transaction_service.dto.TransactionType;
import com.transaction_service.entity.Transaction;
import com.transaction_service.repository.TransactionRepository;
import com.transaction_service.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest // Levanta el contexto de Spring
class TransactionServiceApplicationTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountClient accountClient;

    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionRequest();
        transactionRequest.setAccountOriginId(1L);
        transactionRequest.setAccountDestinationId(2L);
        transactionRequest.setAmount(new BigDecimal("100.00"));
        transactionRequest.setTransactionType(TransactionType.TRANSFER);
        transactionRequest.setClientId(1L);
    }

    @Test
    void processTransaction_ShouldSaveTransaction() {
        TransactionResponse response = transactionService.processTransaction(transactionRequest);

        assertNotNull(response);
        assertEquals(1L, response.getAccountOriginId());
        assertEquals(2L, response.getAccountDestinationId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals(TransactionType.TRANSFER.name(), response.getTransactionType());

        List<Transaction> transactions = transactionRepository.findAll();
        assertFalse(transactions.isEmpty());
    }

    @Test
    void getTransactionHistory_ShouldReturnTransactionList() {
        transactionService.processTransaction(transactionRequest);

        List<TransactionResponse> history = transactionService.getTransactionHistory(1L);

        assertNotNull(history);
        assertFalse(history.isEmpty());
        assertEquals(1L, history.get(0).getAccountOriginId());
    }

    @Test
    void deposit_ShouldSaveTransactionFailed() {
        assertThrows(ResponseStatusException.class, () ->
                transactionService.deposit(transactionRequest)
        );

    }

    @Test
    void withdraw_ShouldSaveTransactionFailed() {

        assertThrows(ResponseStatusException.class, () ->
                transactionService.withdraw(transactionRequest)
        );
    }
}

