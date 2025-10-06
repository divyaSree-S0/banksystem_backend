package com.example.banksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long transactionId;
    private Long accountId;
    private String accountNumber;
    private BigDecimal amount;
    private String type;
    private LocalDateTime dateTime;
    private String description;
}
