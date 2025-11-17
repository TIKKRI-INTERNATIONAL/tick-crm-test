package com.example.demo.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.DB.Message;
import com.example.demo.DB.MessageStatus;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByFromNumber(String fromNumber);
    List<Message> findByToNumber(String toNumber);
    Optional<Message> findByWhatsappMessageId(String whatsappMessageId);
    List<Message> findByStatus(MessageStatus status);
    
    @Query("SELECT m FROM Message m WHERE m.createdAt BETWEEN :startDate AND :endDate")
    List<Message> findMessagesBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);
}
