package com.example.banksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    private String customerName;
    private String phoneNo;
    private String address;
    private String email;
    private String username;
    private String password;
    private String role = "USER"; // Default role
}
