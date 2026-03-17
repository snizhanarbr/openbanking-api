package com.gdnext.openbanking.client;

import com.gdnext.openbanking.domain.dto.request.PaymentRequestDto;
import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import com.gdnext.openbanking.domain.dto.response.TransactionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExternalBankClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ExternalBankClient(RestTemplate restTemplate,
                              @Value("${external-bank.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public AccountBalanceResponseDto getBalance(String iban) {
        String url = baseUrl + "/accounts/" + iban + "/balance";
        return restTemplate.getForObject(url, AccountBalanceResponseDto.class);
    }

    public List<TransactionDto> getTransactions(String iban) {
        String url = baseUrl + "/accounts/" + iban + "/transactions";
        TransactionDto[] transactions = restTemplate.getForObject(url, TransactionDto[].class);
        return transactions != null ? List.of(transactions) : List.of();
    }

    public String initiatePayment(PaymentRequestDto request) {
        String url = baseUrl + "/payments";
        return restTemplate.postForObject(url, request, String.class);
    }
}