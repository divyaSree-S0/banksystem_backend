package com.example.banksystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.banksystem.dto.CreateCustomerRequest;
import com.example.banksystem.dto.CustomerDto;
import com.example.banksystem.entity.Customer;
import com.example.banksystem.exception.ResourceNotFoundException;
import com.example.banksystem.mapper.DtoMapper;
import com.example.banksystem.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repo;
    private final DtoMapper mapper;

    public CustomerDto createCustomer(CreateCustomerRequest request) {
        if (repo.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Customer with email " + request.getEmail() + " already exists");
        }
        
        if (repo.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Customer with username " + request.getUsername() + " already exists");
        }

        Customer customer = mapper.toCustomerEntity(request);
        Customer savedCustomer = repo.save(customer);
        return mapper.toCustomerDto(savedCustomer);
    }

    public Customer saveCustomer(Customer customer) {
        return repo.save(customer);
    }

    public List<CustomerDto> getAllCustomers() {
        return repo.findAll().stream()
                .map(mapper::toCustomerDto)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomerDtoById(Long id) {
        Customer customer = repo.findByCustomerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return mapper.toCustomerDto(customer);
    }

    public Optional<Customer> getCustomerById(Long id) {
        return repo.findByCustomerId(id);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return repo.findByEmail(email);
    }

    public Optional<Customer> getCustomerByUsername(String username) {
        return repo.findByUsername(username);
    }

    public CustomerDto updateCustomer(Long id, CreateCustomerRequest request) {
        Customer customer = repo.findByCustomerId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setCustomerName(request.getCustomerName());
        customer.setPhoneNo(request.getPhoneNo());
        customer.setAddress(request.getAddress());
        customer.setEmail(request.getEmail());

        Customer updatedCustomer = repo.save(customer);
        return mapper.toCustomerDto(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        repo.deleteById(id);
    }
}
