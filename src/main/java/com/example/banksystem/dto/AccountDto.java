package com.example.banksystem.dto;

import com.example.banksystem.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long accountId;
    private String accountNumber;
    private Long balance;
    private AccountType accountType;
    private Long customerId;
    private String customerName;
}
