package com.gdnext.openbanking.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String id;
    private LocalDateTime date;
    private BigDecimal amount;
    private String currency;
}