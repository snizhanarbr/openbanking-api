package com.gdnext.openbanking.controllers;

import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import com.gdnext.openbanking.domain.dto.response.TransactionDto;
import com.gdnext.openbanking.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{iban}/balance")
    @Operation(summary = "Get account balance by IBAN")
    public AccountBalanceResponseDto getBalance(@PathVariable String iban) {
        return accountService.getBalance(iban);
    }

    @GetMapping("/{iban}/transactions")
    @Operation(summary = "Get last 10 transactions for account")
    public List<TransactionDto> getTransactions(@PathVariable String iban) {
        return accountService.getTransactions(iban);
    }
}