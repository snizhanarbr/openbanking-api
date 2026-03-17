package com.gdnext.openbanking.services;

import com.gdnext.openbanking.client.ExternalBankClient;
import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import com.gdnext.openbanking.domain.dto.response.TransactionDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {

    private final ExternalBankClient externalBankClient;

    public AccountService(ExternalBankClient externalBankClient) {
        this.externalBankClient = externalBankClient;
    }

    @Transactional(readOnly = true)
    public AccountBalanceResponseDto getBalance(String iban) {
        return externalBankClient.getBalance(iban);
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> getTransactions(String iban) {
        return externalBankClient.getTransactions(iban);
    }
}