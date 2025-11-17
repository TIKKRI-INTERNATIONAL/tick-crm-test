package com.example.demo.DB;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "whatsapp_message_id")
    private String whatsappMessageId;
    
    @Column(name = "from_number", nullable = false)
    private String fromNumber;
    
    @Column(name = "to_number", nullable = false)
    private String toNumber;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;
    
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
    
    @Column(name = "read_at")
    private LocalDateTime readAt;
    
    // Constructors, Getters, Setters
    public Message() {}
    
    public Message(String fromNumber, String toNumber, String content, MessageType type) {
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
        this.content = content;
        this.type = type;
        this.status = MessageStatus.SENT;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public Long getId() {
        return id;
    }

    public String getWhatsappMessageId() {
        return whatsappMessageId;
    }

    public void setWhatsappMessageId(String whatsappMessageId) {
        this.whatsappMessageId = whatsappMessageId;
    }

    public String getFromNumber() {
        return fromNumber;
    }

    public void setFromNumber(String fromNumber) {
        this.fromNumber = fromNumber;
    }

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public LocalDateTime setDeliveredAt(LocalDateTime now) {
        return now;
    }



}
