package com.gdnext.openbanking.mock;

import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import com.gdnext.openbanking.domain.dto.response.TransactionDto;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/mock-bank")
public class MockBankController {

    @GetMapping("/accounts/{iban}/balance")
    public AccountBalanceResponseDto getBalance(@PathVariable String iban) {
        return new AccountBalanceResponseDto(iban, new BigDecimal("1000.00"));
    }

    @GetMapping("/accounts/{iban}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable String iban) {
        List<TransactionDto> transactions = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= 10; i++) {
            transactions.add(new TransactionDto(
                    "TXN" + i,
                    LocalDateTime.now().minusDays(i),
                    BigDecimal.valueOf(1 + random.nextInt(500)),
                    "EUR"
            ));
        }
        return transactions;
    }

    @PostMapping("/payments")
    public String initiatePayment(@RequestBody Object paymentRequest) {
        return "SUCCESS";
    }
}