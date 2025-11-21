package com.example.demo.Config;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String phoneNumber;

    public LoginRequest(@NotBlank String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LoginRequest() {

    }
    

    // Getters and Setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
