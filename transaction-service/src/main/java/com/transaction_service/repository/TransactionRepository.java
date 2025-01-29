package com.transaction_service.repository;

import com.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountOriginIdOrAccountDestinationId(Long accountId, Long accountId2);
}

