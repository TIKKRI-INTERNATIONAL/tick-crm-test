package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.WebhookRequest;
import com.example.demo.Service.WhatsAppService;

// WebhookController.java
@RestController
@RequestMapping("/webhook")
public class WebhookController {
    
    @Autowired
    private WhatsAppService whatsAppService;
    
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        
        // Verify the token (you should have your own verification logic)
        if ("your_verify_token".equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(403).body("Verification failed");
        }
    }
    
    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookRequest webhookRequest) {
        whatsAppService.handleWebhook(webhookRequest);
        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}
