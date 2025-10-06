package com.example.banksystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.banksystem.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_AccountNumber(String accountNumber);
    List<Transaction> findByAccountAccountIdOrderByDateTimeDesc(Long accountId);
    List<Transaction> findByAccount_Customer_CustomerIdOrderByDateTimeDesc(Long customerId);
}