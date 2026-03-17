package com.gdnext.openbanking.services;

import com.gdnext.openbanking.client.ExternalBankClient;
import com.gdnext.openbanking.domain.dto.request.PaymentRequestDto;
import com.gdnext.openbanking.domain.dto.response.AccountBalanceResponseDto;
import com.gdnext.openbanking.domain.entities.Payment;
import com.gdnext.openbanking.domain.entities.PaymentStatus;
import com.gdnext.openbanking.exceptions.InsufficientFundsException;
import com.gdnext.openbanking.repositories.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ExternalBankClient externalBankClient;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(PaymentRepository paymentRepository, ExternalBankClient externalBankClient) {
        this.paymentRepository = paymentRepository;
        this.externalBankClient = externalBankClient;
    }

    @Transactional
    public Payment initiatePayment(PaymentRequestDto request) {

        AccountBalanceResponseDto balanceResponse = externalBankClient.getBalance(request.getFromIban());

        if (balanceResponse.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Not enough balance");
        }

        Payment payment = Payment.builder()
                .fromIban(request.getFromIban())
                .toIban(request.getToIban())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);

        try {
            externalBankClient.initiatePayment(request);
            payment.setStatus(PaymentStatus.COMPLETED);
        } catch (Exception e) {
            logger.error("Payment failed for IBAN {}: {}", request.getFromIban(), e.getMessage());
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(e.getMessage());
        }

        return paymentRepository.save(payment);
    }
}