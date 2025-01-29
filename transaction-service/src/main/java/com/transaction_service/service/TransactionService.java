package com.transaction_service.service;

import com.transaction_service.client.AccountClient;
import com.transaction_service.dto.TransactionRequest;
import com.transaction_service.dto.TransactionResponse;
import com.transaction_service.dto.TransactionType;
import com.transaction_service.entity.Transaction;
import com.transaction_service.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    public TransactionService(TransactionRepository transactionRepository, AccountClient accountClient) {
        this.transactionRepository = transactionRepository;
        this.accountClient = accountClient;
    }

    public TransactionResponse processTransaction(TransactionRequest request) {

        Transaction transaction = new Transaction();
        transaction.setAccountOriginId(request.getAccountOriginId());
        if (request.getTransactionType().equals(TransactionType.TRANSFER)) {
            transaction.setAccountDestinationId(request.getAccountDestinationId());
        }
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(request.getTransactionType());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return mapToResponse(savedTransaction);
    }

    public List<TransactionResponse> getTransactionHistory(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountOriginIdOrAccountDestinationId(accountId, accountId);
        return transactions.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAccountOriginId(transaction.getAccountOriginId());
        response.setAccountDestinationId(transaction.getAccountDestinationId());
        response.setAmount(transaction.getAmount());
        response.setTransactionType(transaction.getTransactionType().name());
        response.setTransactionDate(transaction.getTransactionDate());
        return response;
    }

    public TransactionResponse transfer(@Valid TransactionRequest request) {
        request.setTransactionType(TransactionType.TRANSFER);
        accountClient.transferAccount(request.getClientId(), request.getAccountOriginId(), request.getAccountDestinationId(), request.getAmount());
        return processTransaction(request);
    }

    public TransactionResponse deposit(@Valid TransactionRequest request) {
        request.setTransactionType(TransactionType.DEPOSIT);
        accountClient.depositAccount(request.getClientId(), request.getAccountOriginId(), request.getAmount());
        return processTransaction(request);
    }

    public TransactionResponse withdraw(@Valid TransactionRequest request) {
        request.setTransactionType(TransactionType.WITHDRAWAL);
        accountClient.withdrawAccount(request.getClientId(), request.getAccountOriginId(), request.getAmount());
        return processTransaction(request);
    }
}

