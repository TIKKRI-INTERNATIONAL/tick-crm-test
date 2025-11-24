package com.example.demo.Controller;

import com.example.demo.Config.JwtResponse;
import com.example.demo.Config.LoginRequest;
import com.example.demo.Config.RegisterRequest;
import com.example.demo.Config.VerifyOTPRequest;
import com.example.demo.Config.WhatsAppMessageResponse;
import com.example.demo.DB.User;
import com.example.demo.DB.UserType;
import com.example.demo.Service.JwtUtils;
import com.example.demo.Service.OTPService;
import com.example.demo.Service.UserService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final OTPService otpService;
    private final JwtUtils jwtUtils;

    public AuthController(UserService userService, OTPService otpService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.otpService = otpService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        Map<String, String> response = new HashMap<>();

        System.out.println("Registering user: " + registerRequest.getUsername());
        System.out.println("Email: " + registerRequest.getEmail());
        System.out.println("Phone Number: " + registerRequest.getPhoneNumber());
        System.out.println("User Type: " + registerRequest.getUserType());

        // Check if username exists
        if (userService.existsByUsername(registerRequest.getUsername())) {
            response.put("message", "Error: Username is already taken!");
            System.out.println("Username already exists: " + registerRequest.getUsername());
            // return ResponseEntity.badRequest().body(response);
            return ResponseEntity.ok(response);
        }

        // Check if email exists
        if (userService.existsByEmail(registerRequest.getEmail())) {
            response.put("message", "Error: Email is already in use!");
            System.out.println("Email already in use: " + registerRequest.getEmail());
            // return ResponseEntity.badRequest().body(response);
            return ResponseEntity.ok(response);
        }

        // Check if phone number exists
        if (userService.existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            response.put("message", "Error: Phone number is already in use!");
           System.out.println("Phone number already in use: " + registerRequest.getPhoneNumber());
            return ResponseEntity.ok(response);
        }

        // Create new user
        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPhoneNumber(),
                registerRequest.getPassword(),
                registerRequest.getUserType() != null ? registerRequest.getUserType() : UserType.CUSTOMER);

        userService.registerUser(user);

        response.put("message", "User registered successfully! Please login with OTP.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        // Check if user exists
        User user = userService.findByPhoneNumber(loginRequest.getPhoneNumber())
                .orElse(null);

        if (user == null) {
            response.put("message", "Error: User not found!");
            return ResponseEntity.ok(response);
        }

        // Send OTP via WhatsApp
        boolean otpSent = otpService.sendOTP(loginRequest.getPhoneNumber());

        if (otpSent) {
            response.put("message", "OTP sent successfully to your WhatsApp!");
            response.put("phoneNumber", loginRequest.getPhoneNumber());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Error: Failed to send OTP. Please try again.");
            return ResponseEntity.ok(response);
        }
    }

    // @PostMapping("/logintemplate")
    // public ResponseEntity<WhatsAppMessageResponse> loginUserTeplte(@Valid @RequestBody LoginRequest loginRequest) {
    //     Map<String, String> response = new HashMap<>();

    //     // Check if user exists
    //     User user = userService.findByPhoneNumber(loginRequest.getPhoneNumber())
    //             .orElse(null);

        // if (user == null) {
        // response.put("message", "Error: User not found!");
        // return ResponseEntity.badRequest().body(response);
        // }

        // Send OTP via WhatsApp
        // boolean otpSent = otpService.sendOTP(loginRequest.getPhoneNumber());

        // if (otpSent) {
        // response.put("message", "OTP sent successfully to your WhatsApp!");
        // return ResponseEntity.ok(response);
        // } else {
        // response.put("message", "Error: Failed to send OTP. Please try again.");
        // return ResponseEntity.badRequest().body(response);
        // }
    //     WhatsAppMessageResponse response = whatsAppService.sendTemplateMessage(request);
    //     return ResponseEntity.ok(response);
    // }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOTPRequest verifyOTPRequest) {
        Map<String, String> response = new HashMap<>();

        // Verify OTP
        boolean isValidOTP = otpService.verifyOTP(
                verifyOTPRequest.getPhoneNumber(),
                verifyOTPRequest.getOtp());

        if (!isValidOTP) {
            response.put("message", "Error: Invalid or expired OTP!");
            return ResponseEntity.ok(response);
        }

        // Get user and enable them
        User user = userService.findByPhoneNumber(verifyOTPRequest.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.enableUser(verifyOTPRequest.getPhoneNumber());

        // Generate JWT token
        String jwt = jwtUtils.generateJwtToken(user.getUsername());

        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserType().name());

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOTP(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();

        // Check if user exists
        if (!userService.existsByPhoneNumber(loginRequest.getPhoneNumber())) {
            response.put("message", "Error: User not found!");
            return ResponseEntity.ok(response);
        }

        // Resend OTP via WhatsApp
        boolean otpSent = otpService.sendOTP(loginRequest.getPhoneNumber());

        if (otpSent) {
            response.put("message", "OTP resent successfully to your WhatsApp!");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Error: Failed to send OTP. Please try again.");
            return ResponseEntity.ok(response);
        }
    }
}
