package com.example.banksystem.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.banksystem.dto.TransactionDto;
import com.example.banksystem.dto.TransferRequest;
import com.example.banksystem.entity.Account;
import com.example.banksystem.entity.Transaction;
import com.example.banksystem.exception.InsufficientFundsException;
import com.example.banksystem.exception.ResourceNotFoundException;
import com.example.banksystem.mapper.DtoMapper;
import com.example.banksystem.repository.AccountRepository;
import com.example.banksystem.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repo;
    private final AccountRepository accountRepository;
    private final DtoMapper mapper;

    public TransactionDto createTransaction(Long accountId, BigDecimal amount, String type, String description) {
        Account account = accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        if ("WITHDRAW".equals(type) && account.getBalance() < amount.longValue()) {
            throw new InsufficientFundsException("Insufficient funds in account");
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setDateTime(LocalDateTime.now());

        // Update account balance
        if ("DEPOSIT".equals(type)) {
            account.setBalance(account.getBalance() + amount.longValue());
        } else if ("WITHDRAW".equals(type)) {
            account.setBalance(account.getBalance() - amount.longValue());
        }

        accountRepository.save(account);
        Transaction savedTransaction = repo.save(transaction);
        return mapper.toTransactionDto(savedTransaction);
    }

    @Transactional
    public TransactionDto transfer(TransferRequest request) {
        Account fromAccount = accountRepository.findByAccountId(request.getFromAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));
        Account toAccount = accountRepository.findByAccountId(request.getToAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if (fromAccount.getBalance() < request.getAmount().longValue()) {
            throw new InsufficientFundsException("Insufficient funds in source account");
        }

        // Update balances
        fromAccount.setBalance(fromAccount.getBalance() - request.getAmount().longValue());
        toAccount.setBalance(toAccount.getBalance() + request.getAmount().longValue());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Create transaction records
        Transaction withdrawTransaction = new Transaction();
        withdrawTransaction.setAccount(fromAccount);
        withdrawTransaction.setAmount(request.getAmount());
        withdrawTransaction.setType("TRANSFER_OUT");
        withdrawTransaction.setDescription("Transfer to " + toAccount.getAccountNumber() + " - " + request.getDescription());
        withdrawTransaction.setDateTime(LocalDateTime.now());

        Transaction depositTransaction = new Transaction();
        depositTransaction.setAccount(toAccount);
        depositTransaction.setAmount(request.getAmount());
        depositTransaction.setType("TRANSFER_IN");
        depositTransaction.setDescription("Transfer from " + fromAccount.getAccountNumber() + " - " + request.getDescription());
        depositTransaction.setDateTime(LocalDateTime.now());

        repo.save(withdrawTransaction);
        repo.save(depositTransaction);

        return mapper.toTransactionDto(withdrawTransaction);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return repo.save(transaction);
    }

    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        return repo.findByAccountAccountIdOrderByDateTimeDesc(accountId).stream()
                .map(mapper::toTransactionDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactionsByCustomerId(Long customerId) {
        return repo.findByAccount_Customer_CustomerIdOrderByDateTimeDesc(customerId).stream()
                .map(mapper::toTransactionDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactionsByAccountNumber(String accountNumber) {
        return repo.findByAccount_AccountNumber(accountNumber).stream()
                .map(mapper::toTransactionDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getAllTransactions() {
        return repo.findAll().stream()
                .map(mapper::toTransactionDto)
                .collect(Collectors.toList());
    }
}
