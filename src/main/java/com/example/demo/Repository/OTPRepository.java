package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.example.demo.DB.OTP;

import jakarta.transaction.Transactional;


@Repository
@Transactional
public interface OTPRepository extends JpaRepository<OTP, Long> {

     Optional<OTP> findByPhoneNumber(String phoneNumber);

    Optional<OTP> findByPhoneNumberAndCodeAndUsedFalse(String phoneNumber, String code);

    @Modifying
    @Query("UPDATE OTP o SET o.used = true WHERE o.phoneNumber = :phoneNumber AND o.used = false")
    void invalidatePreviousOTPs(@Param("phoneNumber") String phoneNumber);

    @Query("SELECT o FROM OTP o WHERE o.phoneNumber = :phoneNumber AND o.used = false AND o.expiresAt > :now ORDER BY o.createdAt DESC")
    Optional<OTP> findLatestValidOTP(@Param("phoneNumber") String phoneNumber, @Param("now") LocalDateTime now);
}
