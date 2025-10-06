package com.example.banksystem.repository;

import com.example.banksystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByAccountId(Long accountId);
    List<Account> findByCustomerCustomerId(Long customerId);
}
