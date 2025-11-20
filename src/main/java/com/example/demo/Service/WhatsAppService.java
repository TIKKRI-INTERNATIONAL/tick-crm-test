package com.example.demo.Service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Config.WebhookRequest;
import com.example.demo.Config.WhatsAppMessageRequest;
import com.example.demo.Config.WhatsAppMessageResponse;
import com.example.demo.DB.Message;
import com.example.demo.DB.MessageStatus;
import com.example.demo.DB.MessageTemplate;
import com.example.demo.DB.MessageType;
import com.example.demo.Repository.MessageRepository;
import com.example.demo.Repository.MessageTemplateRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WhatsAppService {

    @Value("${whatsapp.api.url}")
    private String apiUrl;

    @Value("${whatsapp.api.token}")
    private String apiToken;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageTemplateRepository templateRepository;

    private final RestTemplate restTemplate;

    public WhatsAppService() {
        this.restTemplate = new RestTemplate();
    }

    public WhatsAppMessageResponse sendTextMessage(WhatsAppMessageRequest request) {
        try {
            String url = apiUrl + "/" + phoneNumberId + "/messages";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);

            System.out.println("TTTTTTTTTTTTTT" + request.getTo());
            System.out.println("MMMMMMMMMMMMMM" + request.getMessage());
            System.out.println("PPPPPPPPPPPPPP" + phoneNumberId);

            Map<String, Object> requestBody = Map.of(
                    "messaging_product", "whatsapp",
                    "to", request.getTo(),
                    "type", "TEXT",
                    "text", Map.of("body", request.getMessage()));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            System.out.println("RRRRRRRRRRRRR" + response);

            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();

                // Fix: Handle messages as List instead of Map
                String messageId = null;
                Object messagesObj = responseBody.get("messages");

                if (messagesObj instanceof List) {
                    List<Map<String, Object>> messagesList = (List<Map<String, Object>>) messagesObj;
                    if (!messagesList.isEmpty()) {
                        messageId = (String) messagesList.get(0).get("id");
                    }
                }

                System.out.println("MMMMMMMMMMMMMM" + response.getBody());

                // Save to database only if we got a message ID
                if (messageId != null) {
                    Message message = new Message(phoneNumberId, request.getTo(),
                            request.getMessage(), MessageType.TEXT);
                    message.setWhatsappMessageId(messageId);
                    messageRepository.save(message);

                    return new WhatsAppMessageResponse(true, messageId, "sent", null);
                } else {
                    return new WhatsAppMessageResponse(false, null, "failed", "No message ID in response");
                }
            } else {
                return new WhatsAppMessageResponse(false, null, "failed", "API call failed");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Add this to see the full stack trace
            return new WhatsAppMessageResponse(false, null, "failed", e.getMessage());
        }
    }
    public WhatsAppMessageResponse sendTemplateMessage(WhatsAppMessageRequest request) {
        try {
            Optional<MessageTemplate> templateOpt = templateRepository.findByName(request.getTemplateName());
            if (templateOpt.isEmpty()) {
                return new WhatsAppMessageResponse(false, null, "failed", "Template not found");
            }
    
            MessageTemplate template = templateOpt.get();
            
            // Validate WhatsApp template ID
            if (template.getWhatsappTemplateId() == null || template.getWhatsappTemplateId().trim().isEmpty()) {
                return new WhatsAppMessageResponse(false, null, "failed", "WhatsApp template ID not configured");
            }
    System.out.println(template);
            String url = apiUrl + "/" + phoneNumberId + "/messages";
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);
    
            Map<String, Object> requestBody = buildTemplateRequestBody(request, template);
            System.out.println("Request Body: " + new JSONObject(requestBody).toString(2));
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
    
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
    
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseBody = response.getBody();
                
                // Fix: Properly extract message ID from response
                String messageId = null;
                Object messagesObj = responseBody.get("messages");
                
                if (messagesObj instanceof List) {
                    List<Map<String, Object>> messagesList = (List<Map<String, Object>>) messagesObj;
                    if (!messagesList.isEmpty()) {
                        messageId = (String) messagesList.get(0).get("id");
                    }
                }
    
                // Save to database only if we got a message ID
                if (messageId != null) {
                    Message message = new Message(phoneNumberId, request.getTo(),
                            template.getContent(), MessageType.TEMPLATE);
                    message.setWhatsappMessageId(messageId);
                    messageRepository.save(message);
    
                    return new WhatsAppMessageResponse(true, messageId, "sent", null);
                } else {
                    return new WhatsAppMessageResponse(false, null, "failedthis", "No message ID in response");
                }
            } else {
                return new WhatsAppMessageResponse(false, null, "failed1111", 
                    "API call failed with status: " + response.getStatusCode());
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            return new WhatsAppMessageResponse(false, null, "failed", e.getMessage());
        }
    }
    
    private Map<String, Object> buildTemplateRequestBody(WhatsAppMessageRequest request, MessageTemplate template) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messaging_product", "whatsapp");
        requestBody.put("to", request.getTo());
        requestBody.put("type", "template");

        System.out.println("Building template request body for template: " + template.getWhatsappTemplateId());
    
        Map<String, Object> templateMap = new HashMap<>();
        templateMap.put("name", template.getWhatsappTemplateId());
        // templateMap.put("name", "otp_tick");
    
        // Add language to template if provided
        if (request.getLanguage() != null) {
            Map<String, String> languageMap = new HashMap<>();
            languageMap.put("code", request.getLanguage().getCode());
            // languageMap.put("code", "en_US");
            templateMap.put("language", languageMap);
        }
    
        // Build components for template parameters
        if (request.getTemplateParameters() != null && !request.getTemplateParameters().isEmpty()) {
            List<Map<String, Object>> components = new ArrayList<>();
            
            // Create body component with parameters
            List<Map<String, Object>> bodyParameters = new ArrayList<>();
            
            // Iterate through parameters in order (assuming they're in LinkedHashMap or ordered)
            request.getTemplateParameters().forEach((key, value) -> {
                Map<String, Object> param = new HashMap<>();
                param.put("type", "text");
                param.put("text", value);
                bodyParameters.add(param);
            });
    
            if (!bodyParameters.isEmpty()) {
                Map<String, Object> bodyComponent = new HashMap<>();
                bodyComponent.put("type", "body");
                bodyComponent.put("parameters", bodyParameters);
                components.add(bodyComponent);
            }
    
            // You can add header and button components here if needed
            // Example for header component:
            /*
            if (request.getHeaderParameters() != null && !request.getHeaderParameters().isEmpty()) {
                List<Map<String, Object>> headerParameters = new ArrayList<>();
                request.getHeaderParameters().forEach((key, value) -> {
                    Map<String, Object> param = new HashMap<>();
                    param.put("type", "text");
                    param.put("text", value);
                    headerParameters.add(param);
                });
                
                Map<String, Object> headerComponent = new HashMap<>();
                headerComponent.put("type", "header");
                headerComponent.put("parameters", headerParameters);
                components.add(headerComponent);
            }
            */
    
            templateMap.put("components", components);
        }
        
        requestBody.put("template", templateMap);
        return requestBody;
    }
    // public WhatsAppMessageResponse sendTemplateMessage(WhatsAppMessageRequest request) {
    //     try {
    //         Optional<MessageTemplate> templateOpt = templateRepository.findByName(request.getTemplateName());
    //         if (templateOpt.isEmpty()) {
    //             return new WhatsAppMessageResponse(false, null, "failed3333", "Template not found");
    //         }

    //         MessageTemplate template = templateOpt.get();
    //         String url = apiUrl + "/" + phoneNumberId + "/messages";

    //         HttpHeaders headers = new HttpHeaders();
    //         headers.setContentType(MediaType.APPLICATION_JSON);
    //         headers.setBearerAuth(apiToken);

    //         Map<String, Object> requestBody = buildTemplateRequestBody(request, template);
    //         HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

    //         ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

    //         if (response.getStatusCode().is2xxSuccessful()) {
    //             Map<String, Object> responseBody = response.getBody();
    //             String messageId = (String) ((Map) responseBody.get("messages")).get("id");

    //             Message message = new Message(phoneNumberId, request.getTo(),
    //                     template.getContent(), MessageType.TEMPLATE);
    //             message.setWhatsappMessageId(messageId);
    //             messageRepository.save(message);

    //             return new WhatsAppMessageResponse(true, messageId, "sent", null);
    //         } else {
    //             return new WhatsAppMessageResponse(false, null, "failed4343434", "API call failed");
    //         }

    //     } catch (Exception e) {
    //         return new WhatsAppMessageResponse(false, null, "faildexxpetion", e.getMessage());
    //     }
    // }
    // private Map<String, Object> buildTemplateRequestBody(WhatsAppMessageRequest request, MessageTemplate template) {
    //     Map<String, Object> requestBody = new HashMap<>();
    //     requestBody.put("messaging_product", "whatsapp");
    //     requestBody.put("to", request.getTo());
    //     requestBody.put("type", "template");
    
    //     Map<String, Object> templateMap = new HashMap<>();
    //     templateMap.put("name", template.getWhatsappTemplateId());
    
    //     // Add language to template if provided
    //     if (request.getLanguage() != null) {
    //         Map<String, String> languageMap = new HashMap<>();
    //         languageMap.put("code", request.getLanguage().getCode());
    //         templateMap.put("language", languageMap);
    //     }
    
    //     if (request.getTemplateParameters() != null && !request.getTemplateParameters().isEmpty()) {
    //         List<Map<String, Object>> components = new ArrayList<>();
            
    //         // Create body component with all parameters
    //         List<Map<String, Object>> bodyParameters = new ArrayList<>();
            
    //         request.getTemplateParameters().forEach((key, value) -> {
    //             Map<String, Object> param = new HashMap<>();
    //             param.put("type", "text");
    //             param.put("text", value);
    //             bodyParameters.add(param);
    //         });
    
    //         if (!bodyParameters.isEmpty()) {
    //             Map<String, Object> bodyComponent = new HashMap<>();
    //             bodyComponent.put("type", "body");
    //             bodyComponent.put("parameters", bodyParameters);
    //             components.add(bodyComponent);
    //         }
    
    //         templateMap.put("components", components);
    //     }
        
    //     requestBody.put("template", templateMap);
    //     return requestBody;
    // }
    // private Map<String, Object> buildTemplateRequestBody(WhatsAppMessageRequest request, MessageTemplate template) {
    //     Map<String, Object> requestBody = new HashMap<>();
    //     requestBody.put("messaging_product", "whatsapp");
    //     requestBody.put("to", request.getTo());
    //     requestBody.put("type", "template");

    //     Map<String, Object> templateMap = new HashMap<>();
    //     templateMap.put("name", template.getWhatsappTemplateId());

    //     // Add language to template if provided
    //     if (request.getLanguage() != null) {
    //         Map<String, String> languageMap = new HashMap<>();
    //         languageMap.put("code", request.getLanguage().getCode());
    //         templateMap.put("language", languageMap);
    //     }

    //     if (request.getTemplateParameters() != null && !request.getTemplateParameters().isEmpty()) {
    //         Map<String, Object> parameters = new HashMap<>();
    //         List<Map<String, Object>> components = new ArrayList<>();

    //         request.getTemplateParameters().forEach((key, value) -> {
              
    //             Map<String, Object> param = new HashMap<>();
    //             param.put("type", "text");
    //             param.put("text", value);
    //             components.add(Map.of("type", "body", "parameters", List.of(param)));
    //         });

    //         templateMap.put("components", components);
    //     }
    //     System.out.println(request.getTemplateParameters());
    //     requestBody.put("template", templateMap);
    //     return requestBody;
    // }

    // private Map<String, Object> buildTemplateRequestBody(WhatsAppMessageRequest
    // request, MessageTemplate template) {
    // Map<String, Object> requestBody = new HashMap<>();
    // requestBody.put("messaging_product", "whatsapp");
    // requestBody.put("to", request.getTo());
    // requestBody.put("type", "TEMPLATE");

    // Map<String, Object> templateMap = new HashMap<>();
    // templateMap.put("name", template.getWhatsappTemplateId());

    // if (request.getTemplateParameters() != null &&
    // !request.getTemplateParameters().isEmpty()) {
    // Map<String, Object> parameters = new HashMap<>();
    // List<Map<String, Object>> components = new ArrayList<>();

    // request.getTemplateParameters().forEach((key, value) -> {
    // Map<String, Object> param = new HashMap<>();
    // param.put("type", "text");
    // param.put("text", value);
    // components.add(Map.of("type", "body", "parameters", List.of(param)));
    // });
    // templateMap.put("components", components);
    // }
    // requestBody.put("template", templateMap);
    // return requestBody;
    // }

    public void handleWebhook(WebhookRequest webhookRequest) {
        if (webhookRequest.getEntry() != null) {
            for (WebhookRequest.Entry entry : webhookRequest.getEntry()) {
                if (entry.getChanges() != null) {
                    for (WebhookRequest.Change change : entry.getChanges()) {
                        if (change.getValue() != null) {
                            // Handle incoming messages
                            if (change.getValue().getMessages() != null) {
                                for (WebhookRequest.Message msg : change.getValue().getMessages()) {
                                    handleIncomingMessage(msg);
                                }
                            }

                            // Handle message status updates
                            if (change.getValue().getStatuses() != null) {
                                for (WebhookRequest.Status status : change.getValue().getStatuses()) {
                                    updateMessageStatus(status);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleIncomingMessage(WebhookRequest.Message msg) {
        Message message = new Message(msg.getFrom(), phoneNumberId,
                msg.getText() != null ? msg.getText().getBody() : "Media message",
                MessageType.TEXT);
        message.setWhatsappMessageId(msg.getId());
        messageRepository.save(message);

        // You can add auto-reply logic here
        if (msg.getText() != null) {
            autoReply(msg.getFrom(), msg.getText().getBody());
        }
    }

    private void updateMessageStatus(WebhookRequest.Status status) {
        Optional<Message> messageOpt = messageRepository.findByWhatsappMessageId(status.getId());
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();

            switch (status.getStatus()) {
                case "delivered":
                    message.setStatus(MessageStatus.DELIVERED);
                    message.setDeliveredAt(LocalDateTime.now());
                    break;
                case "read":
                    message.setStatus(MessageStatus.READ);
                    message.setReadAt(LocalDateTime.now());
                    break;
                case "failed":
                    message.setStatus(MessageStatus.FAILED);
                    break;
            }

            messageRepository.save(message);
        }
    }

    private void autoReply(String to, String receivedMessage) {
        // Simple auto-reply logic
        String replyMessage = "Thank you for your message: " + receivedMessage +
                ". We will get back to you soon.";

        WhatsAppMessageRequest request = new WhatsAppMessageRequest();
        request.setTo(to);
        request.setMessage(replyMessage);
        request.setType(MessageType.TEXT);

        sendTextMessage(request);
    }

    public List<Message> getMessagesByPhoneNumber(String phoneNumber) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessagesByPhoneNumber'");
    }

    public List<Message> getAllMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllMessages'");
    }

    public List<Message> getMessagesByPhoneNumber(MessageStatus status) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMessagesByPhoneNumber'");
    }
}
