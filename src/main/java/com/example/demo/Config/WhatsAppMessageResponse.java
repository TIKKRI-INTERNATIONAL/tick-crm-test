package com.example.demo.Config;

public class WhatsAppMessageResponse {
    private boolean success;
    private String messageId;
    private String status;
    private String errorMessage;

    public WhatsAppMessageResponse() {
    }

    public WhatsAppMessageResponse(boolean success, String messageId, String status, String errorMessage) {
        this.success = success;
        this.messageId = messageId;
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
