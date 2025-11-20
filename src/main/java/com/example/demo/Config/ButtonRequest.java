package com.example.demo.Config;

import com.example.demo.DB.ButtonType;

import jakarta.validation.constraints.NotNull;

public class ButtonRequest {
    @NotNull
    private ButtonType type;
    
    private String text;
    private String url;
    private String phoneNumber;


  
    public ButtonRequest(@NotNull ButtonType type, String text, String url, String phoneNumber) {
        this.type = type;
        this.text = text;
        this.url = url;
        this.phoneNumber = phoneNumber;
    }
    public ButtonRequest() {
    }

    public void setType(ButtonType type) {
        this.type = type;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

      public ButtonType getType() {
        return type;
    }
    public String getText() {
        return text;
    }
    public String getUrl() {
        return url;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    // Getters and Setters
}
