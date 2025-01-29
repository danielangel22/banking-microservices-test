package com.account_service;

import com.account_service.dto.AccountRequest;
import com.account_service.dto.AccountResponse;
import com.account_service.dto.AccountType;
import com.account_service.entity.Account;
import com.account_service.exception.InvalidBalanceException;
import com.account_service.repository.AccountRepository;
import com.account_service.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AccountServiceApplicationTests {


    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void createAccount_ShouldReturnAccountResponse() {
        // Arrange
        AccountRequest request = new AccountRequest();
        request.setClientId(1L);
        request.setAccountType(AccountType.SAVINGS);
        request.setBalance(new BigDecimal("1000.00"));

        // Act
        AccountResponse response = accountService.createAccount(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getClientId());
        assertEquals(AccountType.SAVINGS, response.getAccountType());
        assertEquals(new BigDecimal("1000.00"), response.getBalance());
    }

    @Test
    void getAccountById_ShouldReturnAccountResponse() {
        // Arrange
        Account account = new Account();
        account.setClientId(1L);
        account.setAccountType(AccountType.CHECKING);
        account.setBalance(new BigDecimal("500.00"));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        // Act
        AccountResponse response = accountService.getAccountById(savedAccount.getId());

        assertNotNull(response);
        assertEquals(savedAccount.getId(), response.getAccountId());
        assertEquals(AccountType.CHECKING, response.getAccountType());
    }

    @Test
    void depositAccount_ShouldIncreaseBalance() {
        // Arrange
        Account account = new Account();
        account.setClientId(2L);
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("200.00"));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        // Act
        AccountResponse response = accountService.depositAccount(savedAccount.getId(), new BigDecimal("100.00"));

        // Assert
        assertEquals(new BigDecimal("300.00"), response.getBalance());
    }

    @Test
    void withdrawAccount_ShouldDecreaseBalance() {
        // Arrange
        Account account = new Account();
        account.setClientId(2L);
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("500.00"));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        // Act
        AccountResponse response = accountService.withdrawAccount(2L, savedAccount.getId(), new BigDecimal("200.00"));

        // Assert
        assertEquals(new BigDecimal("300.00"), response.getBalance());
    }

    @Test
    void withdrawAccount_ShouldThrowException_WhenInsufficientFunds() {
        // Arrange
        Account account = new Account();
        account.setClientId(2L);
        account.setAccountType(AccountType.SAVINGS);
        account.setBalance(new BigDecimal("100.00"));
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        Account savedAccount = accountRepository.save(account);

        // Act & Assert
        assertThrows(InvalidBalanceException.class, () ->
                accountService.withdrawAccount(2L, savedAccount.getId(), new BigDecimal("200.00"))
        );
    }
}

