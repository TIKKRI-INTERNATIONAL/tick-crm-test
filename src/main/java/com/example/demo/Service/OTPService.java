package com.example.demo.Service;

import com.example.demo.Config.Language;
import com.example.demo.Config.WhatsAppMessageRequest;
import com.example.demo.DB.OTP;
import com.example.demo.Repository.OTPRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    @Value("${otp.expiration.minutes:5}")
    private int otpExpirationMinutes;

    @Value("${otp.length:6}")
    private int otpLength;

    private final OTPRepository otpRepository;
    private final WhatsAppService whatsAppService;
    private final Random random = new Random();

    public OTPService(OTPRepository otpRepository, WhatsAppService whatsAppService) {
        this.otpRepository = otpRepository;
        this.whatsAppService = whatsAppService;
    }

    public String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public boolean sendOTP(String phoneNumber) {
        // Invalidate previous OTPs
        // Optional<OTP> phoneNumbercheck =
        // otpRepository.findByPhoneNumber(phoneNumber);
        // if (!phoneNumbercheck.isEmpty()) {
        otpRepository.invalidatePreviousOTPs(phoneNumber);
        // }

        // Generate new OTP
        String otpCode = generateOTP();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(otpExpirationMinutes);

        // Save OTP to database
        OTP otp = new OTP(phoneNumber, otpCode, expiresAt);
        otpRepository.save(otp);

        Language language = new Language();
        language.setCode("en");

        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setTo(phoneNumber);
        request.setTemplateName("otp_verification");
        request.setOtpCode(otpCode);
        request.setLanguage(language);
        // Send OTP via WhatsApp
        // whatsAppService.sendTemplateMessageOTP(request);
        return whatsAppService.sendOTP(phoneNumber, otpCode);
        // return whatsAppService.sendSimpleOTP(phoneNumber, otpCode);
    }

    public boolean verifyOTP(String phoneNumber, String code) {
        return otpRepository.findByPhoneNumberAndCodeAndUsedFalse(phoneNumber, code)
                .map(otp -> {
                    if (otp.isExpired()) {
                        return false;
                    }
                    otp.setUsed(true);
                    otpRepository.save(otp);
                    return true;
                })
                .orElse(false);
    }
}
