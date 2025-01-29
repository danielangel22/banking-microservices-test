package com.transaction_service.client;

import com.transaction_service.dto.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "account-client", url = "http://localhost:9090/api/accounts")
public interface AccountClient {

    @PutMapping("/deposit/{clientId}")
    ResponseEntity<AccountResponse> depositAccount(@PathVariable Long clientId, @RequestParam Long accountId, @RequestParam BigDecimal amount);

    @PutMapping("/withdraw/{clientId}")
    ResponseEntity<AccountResponse> withdrawAccount(@PathVariable Long clientId, @RequestParam Long accountId, @RequestParam BigDecimal amount);

    @PutMapping("/transfer/{clientId}")
    ResponseEntity<AccountResponse> transferAccount(@PathVariable Long clientId, @RequestParam Long accountId, @RequestParam Long accountDestinationId, @RequestParam BigDecimal amount);
}
