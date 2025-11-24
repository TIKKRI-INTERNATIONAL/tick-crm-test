package com.example.demo.Config;

import java.util.List;
public class WhatsAppApiResponse {
    private List<MessageResponse> messages;
    private Object contacts; // can be refined if needed
    public List<MessageResponse> getMessages() {
        return messages;
    }
    public void setMessages(List<MessageResponse> messages) {
        this.messages = messages;
    }
    public Object getContacts() {
        return contacts;
    }
    public void setContacts(Object contacts) {
        this.contacts = contacts;
    }
}
