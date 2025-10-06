package com.example.banksystem.service;

import com.example.banksystem.dto.AccountDto;
import com.example.banksystem.dto.CreateAccountRequest;
import com.example.banksystem.entity.Account;
import com.example.banksystem.entity.Customer;
import com.example.banksystem.exception.ResourceNotFoundException;
import com.example.banksystem.mapper.DtoMapper;
import com.example.banksystem.repository.AccountRepository;
import com.example.banksystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repo;
    private final CustomerRepository customerRepository;
    private final DtoMapper mapper;

    public AccountDto createAccount(CreateAccountRequest request) {
        Customer customer = customerRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));

        Account account = mapper.toAccountEntity(request);
        account.setCustomer(customer);
        account.setAccountNumber(generateAccountNumber());

        Account savedAccount = repo.save(account);
        return mapper.toAccountDto(savedAccount);
    }

    public Account saveAccount(Account account){
        return repo.save(account);
    }

    public List<AccountDto> listAllAccounts(){
        return repo.findAll().stream()
                .map(mapper::toAccountDto)
                .collect(Collectors.toList());
    }

    public AccountDto getAccountDtoById(Long id) {
        Account account = repo.findByAccountId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return mapper.toAccountDto(account);
    }

    public Optional<Account> getAccountById(Long id) {
        return repo.findByAccountId(id);
    }

    public Optional<Account> getAccountByNumber(String accountNumber){
        return repo.findByAccountNumber(accountNumber);
    }

    public List<AccountDto> getAccountsByCustomerId(Long customerId) {
        return repo.findByCustomerCustomerId(customerId).stream()
                .map(mapper::toAccountDto)
                .collect(Collectors.toList());
    }

    public AccountDto updateBalance(Long accountId, Long newBalance) {
        Account account = repo.findByAccountId(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        account.setBalance(newBalance);
        Account updatedAccount = repo.save(account);
        return mapper.toAccountDto(updatedAccount);
    }

    public void deleteAccount(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        repo.deleteById(id);
    }

    private String generateAccountNumber() {
        return "ACC" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
