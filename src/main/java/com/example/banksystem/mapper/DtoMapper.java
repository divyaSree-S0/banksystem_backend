package com.example.banksystem.mapper;

import org.springframework.stereotype.Component;

import com.example.banksystem.dto.AccountDto;
import com.example.banksystem.dto.CreateAccountRequest;
import com.example.banksystem.dto.CreateCustomerRequest;
import com.example.banksystem.dto.CustomerDto;
import com.example.banksystem.dto.TransactionDto;
import com.example.banksystem.entity.Account;
import com.example.banksystem.entity.Customer;
import com.example.banksystem.entity.Transaction;

@Component
public class DtoMapper {

    public CustomerDto toCustomerDto(Customer customer) {
        if (customer == null) return null;
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getPhoneNo(),
                customer.getAddress(),
                customer.getEmail(),
                customer.getUsername(),
                customer.getRole()
        );
    }

    public Customer toCustomerEntity(CreateCustomerRequest request) {
        if (request == null) return null;
        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setPhoneNo(request.getPhoneNo());
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());
        customer.setUsername(request.getUsername());
        customer.setPassword(request.getPassword());
        customer.setRole(request.getRole());
        return customer;
    }

    public AccountDto toAccountDto(Account account) {
        if (account == null) return null;
        return new AccountDto(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getCustomer().getCustomerId(),
                account.getCustomer().getCustomerName()
        );
    }

    public Account toAccountEntity(CreateAccountRequest request) {
        if (request == null) return null;
        Account account = new Account();
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialBalance());
        return account;
    }

    public TransactionDto toTransactionDto(Transaction transaction) {
        if (transaction == null) return null;
        return new TransactionDto(
                transaction.getTransactionId(),
                transaction.getAccount().getAccountId(),
                transaction.getAccount().getAccountNumber(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getDateTime(),
                transaction.getDescription()
        );
    }
}
