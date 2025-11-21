package com.example.demo.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Config.CreateTemplateRequest;
import com.example.demo.DB.MessageTemplate;
import com.example.demo.DB.TemplateStatus;
import com.example.demo.Service.MessageTemplateService;

import jakarta.validation.Valid;

// MessageTemplateController.java
@RestController
@RequestMapping("/api/whatsapp/templates")
@Validated
public class MessageTemplateController {
    
    private final MessageTemplateService templateService;
    
    public MessageTemplateController(MessageTemplateService templateService) {
        this.templateService = templateService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<MessageTemplate> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request) {
        MessageTemplate template = templateService.createTemplate(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(template);
    }
    
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<MessageTemplate>> getTemplatesByAccount(
            @PathVariable String accountId) {
        List<MessageTemplate> templates = templateService.getTemplatesByAccount(accountId);
        return ResponseEntity.ok(templates);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MessageTemplate> getTemplate(@PathVariable String id) {
        return templateService.getTemplate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<MessageTemplate> updateTemplateStatus(
            @PathVariable String id,
            @RequestParam TemplateStatus status) {
        MessageTemplate template = templateService.updateTemplateStatus(id, status);
        return ResponseEntity.ok(template);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
