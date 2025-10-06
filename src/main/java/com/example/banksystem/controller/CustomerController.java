package com.example.banksystem.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banksystem.dto.CreateCustomerRequest;
import com.example.banksystem.dto.CustomerDto;
import com.example.banksystem.dto.LoginRequest;
import com.example.banksystem.entity.Customer;
import com.example.banksystem.service.CustomerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4200"})
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CreateCustomerRequest request) {
        CustomerDto customerDto = service.createCustomer(request);
        return new ResponseEntity<>(customerDto, HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    public ResponseEntity<CustomerDto> createAdmin(@RequestBody CreateCustomerRequest request) {
        // Force role to be ADMIN
        request.setRole("ADMIN");
        CustomerDto customerDto = service.createCustomer(request);
        return new ResponseEntity<>(customerDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(service.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable Long id) {
        CustomerDto customerDto = service.getCustomerDtoById(id);
        return ResponseEntity.ok(customerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody CreateCustomerRequest request) {
        CustomerDto updatedCustomer = service.updateCustomer(id, request);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        return service.getCustomerByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

        @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Customer customer = service.getCustomerByUsername(loginRequest.getUsername())
                    .orElse(service.getCustomerByEmail(loginRequest.getUsername()).orElse(null));
            
            if (customer != null && customer.getPassword().equals(loginRequest.getPassword())) {
                // Generate a simple token (in production, use JWT)
                String token = "token_" + customer.getCustomerId() + "_" + System.currentTimeMillis();
                
                // Create user object for response
                Map<String, Object> userResponse = Map.of(
                    "customerId", customer.getCustomerId(),
                    "username", customer.getUsername(),
                    "email", customer.getEmail(),
                    "customerName", customer.getCustomerName(),
                    "role", customer.getRole()
                );
                
                Map<String, Object> response = Map.of(
                    "token", token,
                    "user", userResponse,
                    "expiresIn", 3600
                );
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Login failed: " + e.getMessage()));
        }
    }
}
