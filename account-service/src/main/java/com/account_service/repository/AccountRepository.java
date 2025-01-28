package com.account_service.repository;

import com.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByClientId(Long clientId);
    Account findByIdAndClientId(Long accountId, Long clientId);
}

