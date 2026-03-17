package com.gdnext.openbanking.repositories;

import com.gdnext.openbanking.domain.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}