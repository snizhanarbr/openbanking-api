package com.gdnext.openbanking.services;

import com.gdnext.openbanking.client.ExternalBankClient;
import com.gdnext.openbanking.domain.dto.request.PaymentRequestDto;
import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import com.gdnext.openbanking.domain.entities.Payment;
import com.gdnext.openbanking.domain.entities.PaymentStatus;
import com.gdnext.openbanking.exceptions.InsufficientFundsException;
import com.gdnext.openbanking.repositories.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ExternalBankClient externalBankClient;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldFailWhenBalanceTooLow() {

        PaymentRequestDto request = new PaymentRequestDto();
        request.setFromIban("DE123");
        request.setToIban("FR999");
        request.setAmount(new BigDecimal("500"));
        request.setCurrency("EUR");

        AccountBalanceResponseDto balance = new AccountBalanceResponseDto("DE123", new BigDecimal("100"));

        when(externalBankClient.getBalance("DE123")).thenReturn(balance);

        assertThrows(InsufficientFundsException.class,
                () -> paymentService.initiatePayment(request));
    }

    @Test
    void shouldCreatePaymentWhenBalanceEnough() {

        PaymentRequestDto request = new PaymentRequestDto();
        request.setFromIban("DE123");
        request.setToIban("FR999");
        request.setAmount(new BigDecimal("100"));
        request.setCurrency("EUR");

        AccountBalanceResponseDto balance =
                new AccountBalanceResponseDto("DE123", new BigDecimal("1000"));

        when(externalBankClient.getBalance("DE123")).thenReturn(balance);

        Payment savedPayment = Payment.builder()
                .fromIban("DE123")
                .toIban("FR999")
                .amount(new BigDecimal("100"))
                .currency("EUR")
                .status(PaymentStatus.PENDING)
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        Payment result = paymentService.initiatePayment(request);

        assertNotNull(result);
        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
    }

    @Test
    void shouldMarkPaymentFailedWhenExternalFails() {

        PaymentRequestDto request = new PaymentRequestDto();
        request.setFromIban("DE123");
        request.setToIban("FR999");
        request.setAmount(new BigDecimal("100"));
        request.setCurrency("EUR");

        AccountBalanceResponseDto balance =
                new AccountBalanceResponseDto("DE123", new BigDecimal("1000"));

        when(externalBankClient.getBalance("DE123")).thenReturn(balance);
        when(externalBankClient.initiatePayment(request)).thenThrow(new RuntimeException("Bank error"));

        ArgumentCaptor<Payment> captor = ArgumentCaptor.forClass(Payment.class);
        when(paymentRepository.save(captor.capture())).thenAnswer(i -> i.getArgument(0));

        Payment result = paymentService.initiatePayment(request);

        assertEquals(PaymentStatus.FAILED, result.getStatus());
        assertEquals("Bank error", result.getFailureReason());
    }
}