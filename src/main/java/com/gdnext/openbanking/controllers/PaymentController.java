package com.gdnext.openbanking.controllers;

import com.gdnext.openbanking.domain.dto.request.PaymentRequestDto;
import com.gdnext.openbanking.domain.dto.response.PaymentResponseDto;
import com.gdnext.openbanking.domain.entities.Payment;
import com.gdnext.openbanking.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    @Operation(summary = "Initiate a new IBAN to IBAN payment")
    public PaymentResponseDto initiatePayment(@Valid @RequestBody PaymentRequestDto request) {

        Payment payment = paymentService.initiatePayment(request);

        return new PaymentResponseDto(
                payment.getId().toString(),
                payment.getStatus().name()
        );
    }
}