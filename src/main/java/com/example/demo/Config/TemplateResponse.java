package com.example.demo.Config;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.DB.TemplateCategory;
import com.example.demo.DB.TemplateStatus;

public class TemplateResponse {
    private String id;
    private String name;
    private TemplateCategory category;
    private String language;
    private List<ComponentRequest> components;
    private String whatsappBusinessAccountId;
    private TemplateStatus status;
    private LocalDateTime createdAt;

  
    public TemplateResponse(String id, String name, TemplateCategory category, String language,
            List<ComponentRequest> components, String whatsappBusinessAccountId, TemplateStatus status,
            LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.language = language;
        this.components = components;
        this.whatsappBusinessAccountId = whatsappBusinessAccountId;
        this.status = status;
        this.createdAt = createdAt;
    }
    public TemplateResponse() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(TemplateCategory category) {
        this.category = category;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setComponents(List<ComponentRequest> components) {
        this.components = components;
    }

    public void setWhatsappBusinessAccountId(String whatsappBusinessAccountId) {
        this.whatsappBusinessAccountId = whatsappBusinessAccountId;
    }

    public void setStatus(TemplateStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

      public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public TemplateCategory getCategory() {
        return category;
    }
    public String getLanguage() {
        return language;
    }
    public List<ComponentRequest> getComponents() {
        return components;
    }
    public String getWhatsappBusinessAccountId() {
        return whatsappBusinessAccountId;
    }
    public TemplateStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Constructors, Getters, Setters
}