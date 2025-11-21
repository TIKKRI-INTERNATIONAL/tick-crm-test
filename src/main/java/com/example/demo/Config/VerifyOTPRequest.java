package com.example.demo.Config;

// VerifyOTPRequest.java

import jakarta.validation.constraints.NotBlank;

public class VerifyOTPRequest {
    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String otp;

    public VerifyOTPRequest(@NotBlank String phoneNumber, @NotBlank String otp) {
        this.phoneNumber = phoneNumber;
        this.otp = otp;
    }

    public VerifyOTPRequest() {

    }
    

    // Getters and Setters
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
}
