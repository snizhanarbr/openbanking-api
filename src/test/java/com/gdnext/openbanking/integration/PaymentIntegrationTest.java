package com.gdnext.openbanking.integration;

import com.gdnext.openbanking.client.ExternalBankClient;
import com.gdnext.openbanking.domain.dto.request.PaymentRequestDto;
import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ExternalBankClient externalBankClient;

    @Test
    void shouldCreatePayment() {

        AccountBalanceResponseDto balance = new AccountBalanceResponseDto();
        balance.setIban("DE123456");
        balance.setBalance(new BigDecimal("1000"));

        when(externalBankClient.getBalance("DE123456"))
                .thenReturn(balance);

        PaymentRequestDto request = new PaymentRequestDto();
        request.setFromIban("DE123456");
        request.setToIban("FR999999");
        request.setAmount(new BigDecimal("100"));
        request.setCurrency("EUR");

        String url = "http://localhost:" + port + "/api/payments/initiate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentRequestDto> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void shouldFailWhenInsufficientFunds() {

        AccountBalanceResponseDto balance = new AccountBalanceResponseDto();
        balance.setIban("DE123456");
        balance.setBalance(new BigDecimal("50"));

        when(externalBankClient.getBalance("DE123456"))
                .thenReturn(balance);

        PaymentRequestDto request = new PaymentRequestDto();
        request.setFromIban("DE123456");
        request.setToIban("FR999999");
        request.setAmount(new BigDecimal("100"));
        request.setCurrency("EUR");

        String url = "http://localhost:" + port + "/api/payments/initiate";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentRequestDto> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(url, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void shouldReturnAccountBalance() {

        AccountBalanceResponseDto balance = new AccountBalanceResponseDto();
        balance.setIban("DE123456");
        balance.setBalance(new BigDecimal("1000"));

        when(externalBankClient.getBalance("DE123456"))
                .thenReturn(balance);

        String url = "http://localhost:" + port + "/api/accounts/DE123456/balance";

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}