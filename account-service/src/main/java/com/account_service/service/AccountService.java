package com.account_service.service;


import com.account_service.dto.AccountRequest;
import com.account_service.dto.AccountResponse;
import com.account_service.entity.Account;
import com.account_service.exception.InvalidBalanceException;
import com.account_service.exception.NotFoundDataException;
import com.account_service.repository.AccountRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public AccountResponse createAccount(AccountRequest accountRequest) {

        Account account = new Account();
        account.setClientId(accountRequest.getClientId());
        account.setAccountType(accountRequest.getAccountType());
        account.setBalance(accountRequest.getBalance());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        return mapToAccountResponse(savedAccount);
    }


    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundDataException("Account not found with ID: " + accountId));
        return mapToAccountResponse(account);
    }


    public List<AccountResponse> getAccountsByClientId(Long clientId) {
        List<Account> accounts = accountRepository.findByClientId(clientId);
        if (accounts.isEmpty()) throw new NotFoundDataException("Not found data for client : " + clientId);
        return accounts.stream()
                .map(this::mapToAccountResponse)
                .collect(Collectors.toList());
    }

    private AccountResponse mapToAccountResponse(Account account) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountId(account.getId());
        accountResponse.setClientId(account.getClientId());
        accountResponse.setAccountType(account.getAccountType());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setCreatedAt(account.getCreatedAt());
        accountResponse.setUpdatedAt(account.getUpdatedAt());
        return accountResponse;
    }

    public AccountResponse depositAccount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new NotFoundDataException("No se encontro una cuenta asociada a ese cliente"));
        account.setBalance(account.getBalance().add(amount));
        var response = accountRepository.save(account);
        return mapToAccountResponse(response);
    }

    public AccountResponse withdrawAccount(Long clientId, Long accountId, BigDecimal amount) {
        Account account = accountRepository.findByIdAndClientId(accountId, clientId);
        if (account == null) throw new NotFoundDataException("No se encontro una cuenta asociada a ese cliente");

        BigDecimal resultingBalance = account.getBalance().subtract(amount);
        if (resultingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("No cuentas con suficientes fondos para realizar el retiro.");
        }
        account.setBalance(account.getBalance().subtract(amount));
        var response = accountRepository.save(account);
        return mapToAccountResponse(response);
    }

    @Transactional
    public AccountResponse transferAccount(Long clientId, Long accountId, Long accountDestinationId, BigDecimal amount) {
        Account accountOrigin = accountRepository.findByIdAndClientId(accountId, clientId);
        if (accountOrigin == null) throw new NotFoundDataException("No se encontro una cuenta asociada a ese cliente");

        BigDecimal resultingBalance = accountOrigin.getBalance().subtract(amount);
        if (resultingBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidBalanceException("No cuentas con suficientes fondos para realizar el retiro.");
        }

        accountOrigin.setBalance(accountOrigin.getBalance().subtract(amount));
        var responseOrigin = accountRepository.save(accountOrigin);
        //agregar el monto a la cuenta destino
        Account accountDestination = accountRepository.findById(accountDestinationId).orElseThrow(() -> new NotFoundDataException("No se encontro la cuenta destino"));
        accountDestination.setBalance(accountDestination.getBalance().add(amount));
        accountRepository.save(accountDestination);
        return mapToAccountResponse(responseOrigin);
    }
}

