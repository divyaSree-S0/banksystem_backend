package com.example.banksystem.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banksystem.dto.DepositRequest;
import com.example.banksystem.dto.TransactionDto;
import com.example.banksystem.dto.TransferRequest;
import com.example.banksystem.dto.WithdrawRequest;
import com.example.banksystem.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4200"})
public class TransactionController {

    private final TransactionService service;

    @PostMapping("/deposit")
    public ResponseEntity<TransactionDto> deposit(@RequestBody DepositRequest request) {
        TransactionDto transaction = service.createTransaction(
                request.getAccountId(),
                request.getAmount(),
                "DEPOSIT",
                request.getDescription()
        );
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionDto> withdraw(@RequestBody WithdrawRequest request) {
        TransactionDto transaction = service.createTransaction(
                request.getAccountId(),
                request.getAmount(),
                "WITHDRAW",
                request.getDescription()
        );
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDto> transfer(@RequestBody TransferRequest request) {
        TransactionDto transaction = service.transfer(request);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountId(@PathVariable Long accountId) {
        List<TransactionDto> transactions = service.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDto>> getTransactionsByCustomerId(@PathVariable Long customerId) {
        List<TransactionDto> transactions = service.getTransactionsByCustomerId(customerId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        List<TransactionDto> transactions = service.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }
}



//package com.example.banksystem.controller;
//
//import com.example.banksystem.dto.TransactionDto;
//import com.example.banksystem.dto.TransferRequest;
//import com.example.banksystem.entity.Transaction;
//import com.example.banksystem.service.TransactionService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/transactions")
//@RequiredArgsConstructor
//public class TransactionController {
//
//    private final TransactionService service;
//
//    @PostMapping("/deposit")
//    public ResponseEntity<TransactionDto> deposit(
//            @RequestParam Long accountId,
//            @RequestParam BigDecimal amount,
//            @RequestParam(required = false) String description) {
//        TransactionDto transaction = service.createTransaction(accountId, amount, "DEPOSIT", description);
//        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/withdraw")
//    public ResponseEntity<TransactionDto> withdraw(
//            @RequestParam Long accountId,
//            @RequestParam BigDecimal amount,
//            @RequestParam(required = false) String description) {
//        TransactionDto transaction = service.createTransaction(accountId, amount, "WITHDRAW", description);
//        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
//    }
//
//    @PostMapping("/transfer")
//    public ResponseEntity<TransactionDto> transfer(@RequestBody TransferRequest request) {
//        TransactionDto transaction = service.transfer(request);
//        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
//    }
//
//    @PostMapping
//    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
//        return ResponseEntity.ok(service.saveTransaction(transaction));
//    }
//
//    @GetMapping("/account/{accountId}")
//    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountId(@PathVariable Long accountId) {
//        List<TransactionDto> transactions = service.getTransactionsByAccountId(accountId);
//        return ResponseEntity.ok(transactions);
//    }
//
//    @GetMapping("/account/number/{accountNumber}")
//    public ResponseEntity<List<TransactionDto>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
//        List<TransactionDto> transactions = service.getTransactionsByAccountNumber(accountNumber);
//        return ResponseEntity.ok(transactions);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
//        List<TransactionDto> transactions = service.getAllTransactions();
//        return ResponseEntity.ok(transactions);
//    }
//}
