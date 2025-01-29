package com.account_service.repository;

import com.account_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
