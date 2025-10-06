package com.example.banksystem.dto;

import com.example.banksystem.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    private Long customerId;
    private AccountType accountType;
    private Long initialBalance;
}
