package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.WhatsAppMessageRequest;
import com.example.demo.Config.WhatsAppMessageResponse;
import com.example.demo.DB.Message;
import com.example.demo.DB.MessageStatus;
import com.example.demo.Service.WhatsAppService;

// WhatsAppController.java
@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {
    
    @Autowired
    private WhatsAppService whatsAppService;
    
    @PostMapping("/send-text")
    public ResponseEntity<WhatsAppMessageResponse> sendTextMessage(@RequestBody WhatsAppMessageRequest request) {
        WhatsAppMessageResponse response = whatsAppService.sendTextMessage(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/send-template")
    public ResponseEntity<WhatsAppMessageResponse> sendTemplateMessage(@RequestBody WhatsAppMessageRequest request) {
        WhatsAppMessageResponse response = whatsAppService.sendTemplateMessage(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages(
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) MessageStatus status) {
        
        List<Message> messages;
        if (phoneNumber != null) {
            messages = whatsAppService.getMessagesByPhoneNumber(phoneNumber);
        } else if (status != null) {
            messages = whatsAppService.getMessagesByPhoneNumber(status);
        } else {
            messages = whatsAppService.getAllMessages();
        }
        
        return ResponseEntity.ok(messages);
    }
}


