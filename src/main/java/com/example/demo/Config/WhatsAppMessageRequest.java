package com.example.demo.Config;

import java.util.Map;
import com.example.demo.DB.MessageType;

public class WhatsAppMessageRequest {
    private String to;
    private String message;
    private Language language; // Changed from String to Language object
    private MessageType type;
    private String templateName;
    private Map<String, String> templateParameters;

    public WhatsAppMessageRequest(String to, String message, Language language, MessageType type, String templateName,
            Map<String, String> templateParameters) {
        this.to = to;
        this.message = message;
        this.language = language;
        this.type = type;
        this.templateName = templateName;
        this.templateParameters = templateParameters;
    }

    public WhatsAppMessageRequest() {
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    // ... other getters and setters remain the same
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, String> getTemplateParameters() {
        return templateParameters;
    }

    public void setTemplateParameters(Map<String, String> templateParameters) {
        this.templateParameters = templateParameters;
    }
}