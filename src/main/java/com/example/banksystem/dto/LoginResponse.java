package com.example.banksystem.dto;

public class LoginResponse {
    private String token;
    private CustomerDto user;
    private int expiresIn;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, CustomerDto user, int expiresIn) {
        this.token = token;
        this.user = user;
        this.expiresIn = expiresIn;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomerDto getUser() {
        return user;
    }

    public void setUser(CustomerDto user) {
        this.user = user;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}