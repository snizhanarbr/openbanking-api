package com.gdnext.openbanking.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceResponseDto {
    private String iban;
    private BigDecimal balance;
}