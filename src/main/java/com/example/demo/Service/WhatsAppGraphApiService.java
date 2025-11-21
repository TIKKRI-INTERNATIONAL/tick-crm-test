package com.example.demo.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Service
public class WhatsAppGraphApiService {

    @Value("${whatsapp.api.url}")
    private String graphApiBaseUrl;

    @Value("${whatsapp.api.token}")
    private String accessToken;

    private final WebClient webClient;

    public WhatsAppGraphApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(graphApiBaseUrl).build();
    }

    public Mono<String> createMessageTemplate(String whatsappBusinessAccountId,
            Map<String, Object> templateData) {
        String url = "/" + whatsappBusinessAccountId + "/message_templates";

        return webClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(templateData)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(error -> new RuntimeException("Failed to create template: " + error.getMessage()));
    }

    public Mono<String> getMessageTemplates(String whatsappBusinessAccountId) {
        String url = "/104012505768129/message_templates";

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("access_token", accessToken)
                        .build(whatsappBusinessAccountId))
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> deleteMessageTemplate(String templateId) {
        String url = "/{template-id}";

        return webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("access_token", accessToken)
                        .build(templateId))
                .retrieve()
                .bodyToMono(String.class);
    }
}
