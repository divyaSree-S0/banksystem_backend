package com.example.banksystem.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class WithdrawRequest {
    private Long accountId;
    private BigDecimal amount;
    private String description;
}
