package com.example.demo.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.Config.ButtonRequest;
import com.example.demo.Config.ComponentRequest;
import com.example.demo.Config.CreateTemplateRequest;
import com.example.demo.DB.Button;
import com.example.demo.DB.MessageTemplate;
import com.example.demo.DB.TemplateComponent;
import com.example.demo.DB.TemplateStatus;
import com.example.demo.Repository.MessageTemplateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MessageTemplateService {

    private final MessageTemplateRepository templateRepository;
    private final WhatsAppGraphApiService graphApiService;
    private final ObjectMapper objectMapper;

    public MessageTemplateService(MessageTemplateRepository templateRepository,
            WhatsAppGraphApiService graphApiService,
            ObjectMapper objectMapper) {
        this.templateRepository = templateRepository;
        this.graphApiService = graphApiService;
        this.objectMapper = objectMapper;
    }

    public MessageTemplate createTemplate(CreateTemplateRequest request) {
        System.out.println("Creating template: " + request.getName());
        // Check if template with same name exists for this account
        templateRepository.findByNameAndWhatsappBusinessAccountId(
                request.getName(), request.getWhatsappBusinessAccountId())
                .ifPresent(template -> {
                    throw new RuntimeException("Template with name '" + request.getName() +
                            "' already exists for this account");
                });

        // Convert to Graph API format
        Map<String, Object> graphApiRequest = convertToGraphApiFormat(request);

        // Call Graph API
        String response = graphApiService.createMessageTemplate(
                request.getWhatsappBusinessAccountId(), graphApiRequest).block();

        // Parse response and save to database
        try {
            JsonNode responseNode = objectMapper.readTree(response);
            String templateId = responseNode.get("id").asText();

            MessageTemplate template = new MessageTemplate();
            template.setId(Long.parseLong(templateId));
            template.setName(request.getName());
            template.setCategory(request.getCategory());
            template.setLanguage(request.getLanguage());
            template.setWhatsappBusinessAccountId(request.getWhatsappBusinessAccountId());
            template.setStatus(TemplateStatus.PENDING);

            // Convert components
            List<TemplateComponent> components = request.getComponents().stream()
                    .map(this::convertComponent)
                    .collect(Collectors.toList());
            // template.setComponents(components);

            return templateRepository.save(template);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Graph API response", e);
        }
    }

    public List<MessageTemplate> getTemplatesByAccount(String accountId) {
        return templateRepository.findByWhatsappBusinessAccountId(accountId);
    }

    public Optional<MessageTemplate> getTemplate(String id) {
        return templateRepository.findById(Long.parseLong(id));
    }

    public void deleteTemplate(String id) {
        MessageTemplate template = templateRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Template not found"));

        // Delete from Graph API
        graphApiService.deleteMessageTemplate(id).block();

        // Delete from database
        templateRepository.delete(template);
    }

    public MessageTemplate updateTemplateStatus(String id, TemplateStatus status) {
        MessageTemplate template = templateRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setStatus(status);
        return templateRepository.save(template);
    }

    private Map<String, Object> convertToGraphApiFormat(CreateTemplateRequest request) {
        Map<String, Object> graphApiRequest = new HashMap<>();
        graphApiRequest.put("name", request.getName());
        graphApiRequest.put("category", request.getCategory().name().toLowerCase());
        graphApiRequest.put("language", request.getLanguage());

        List<Map<String, Object>> components = request.getComponents().stream()
                .map(this::convertComponentToGraphApiFormat)
                .collect(Collectors.toList());
        graphApiRequest.put("components", components);

        return graphApiRequest;
    }

    private Map<String, Object> convertComponentToGraphApiFormat(ComponentRequest component) {
        Map<String, Object> compMap = new HashMap<>();
        compMap.put("type", component.getType().name().toLowerCase());

        if (component.getText() != null) {
            compMap.put("text", component.getText());
        }

        if (component.getButtons() != null && !component.getButtons().isEmpty()) {
            List<Map<String, Object>> buttons = component.getButtons().stream()
                    .map(this::convertButtonToGraphApiFormat)
                    .collect(Collectors.toList());
            compMap.put("buttons", buttons);
        }

        if (component.getExample() != null) {
            compMap.put("example", component.getExample());
        }

        return compMap;
    }

    private Map<String, Object> convertButtonToGraphApiFormat(ButtonRequest button) {
        Map<String, Object> buttonMap = new HashMap<>();
        buttonMap.put("type", button.getType().name().toLowerCase());

        switch (button.getType()) {
            case QUICK_REPLY:
                buttonMap.put("text", button.getText());
                break;
            case URL:
                buttonMap.put("text", button.getText());
                buttonMap.put("url", button.getUrl());
                break;
            case PHONE_NUMBER:
                buttonMap.put("text", button.getText());
                buttonMap.put("phone_number", button.getPhoneNumber());
                break;
        }

        return buttonMap;
    }

    private TemplateComponent convertComponent(ComponentRequest compRequest) {
        TemplateComponent component = new TemplateComponent();
        component.setType(compRequest.getType());
        component.setText(compRequest.getText());

        if (compRequest.getExample() != null) {
            try {
                component.setExampleJson(objectMapper.writeValueAsString(compRequest.getExample()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize example", e);
            }
        }

        if (compRequest.getButtons() != null) {
            List<Button> buttons = compRequest.getButtons().stream()
                    .map(this::convertButton)
                    .collect(Collectors.toList());
            component.setButtons(buttons);
        }

        return component;
    }

    private Button convertButton(ButtonRequest buttonRequest) {
        Button button = new Button();
        button.setType(String.valueOf(buttonRequest.getType()));
        button.setText(buttonRequest.getText());
        button.setUrl(buttonRequest.getUrl());
        button.setPhoneNumber(buttonRequest.getPhoneNumber());
        return button;
    }
}
