package com.example.demo.DB;

import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "message_templates")
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private TemplateCategory category;

    @Column(name = "whatsapp_template_id")
    private String whatsappTemplateId;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String language;

    @Enumerated(EnumType.STRING)
    private TemplateStatus status;

    @Column(name = "whatsapp_business_account_id")
    private String whatsappBusinessAccountId;

    public MessageTemplate(Long id, String name, String content, TemplateCategory category, String whatsappTemplateId,
            Boolean isActive, LocalDateTime createdAt, String language, TemplateStatus status,
            String whatsappBusinessAccountId) {
        this.id = id;
        this.name = name;
        this.content = content;
        this.category = category;
        this.whatsappTemplateId = whatsappTemplateId;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.language = language;
        this.status = status;
        this.whatsappBusinessAccountId = whatsappBusinessAccountId;
    }

    public MessageTemplate() {
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public TemplateStatus getStatus() {
        return status;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }

    public String getWhatsappBusinessAccountId() {
        return whatsappBusinessAccountId;
    }

    public void setWhatsappBusinessAccountId(String whatsappBusinessAccountId) {
        this.whatsappBusinessAccountId = whatsappBusinessAccountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TemplateCategory getCategory() {
        return category;
    }

    public void setCategory(TemplateCategory category) {
        this.category = category;
    }

    public String getWhatsappTemplateId() {
        return whatsappTemplateId;
    }

    public void setWhatsappTemplateId(String whatsappTemplateId) {
        this.whatsappTemplateId = whatsappTemplateId;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
