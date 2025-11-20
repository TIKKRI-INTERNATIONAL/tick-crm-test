package com.example.demo.Config;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

import com.example.demo.DB.TemplateCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateTemplateRequest {
    @NotBlank
    private String name;

    @NotNull
    private TemplateCategory category;

    @NotBlank
    private String language;

    @Valid
    @NotEmpty
    private List<ComponentRequest> components;

    @NotBlank
    private String whatsappBusinessAccountId;

    public CreateTemplateRequest(@NotBlank String name, @NotNull TemplateCategory category, @NotBlank String language,
            @Valid @NotEmpty List<ComponentRequest> components, @NotBlank String whatsappBusinessAccountId) {
        this.name = name;
        this.category = category;
        this.language = language;
        this.components = components;
        this.whatsappBusinessAccountId = whatsappBusinessAccountId;
    }

    public CreateTemplateRequest() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TemplateCategory getCategory() {
        return category;
    }

    public void setCategory(TemplateCategory category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<ComponentRequest> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentRequest> components) {
        this.components = components;
    }

    public String getWhatsappBusinessAccountId() {
        return whatsappBusinessAccountId;
    }

    public void setWhatsappBusinessAccountId(String whatsappBusinessAccountId) {
        this.whatsappBusinessAccountId = whatsappBusinessAccountId;
    }

    // Getters and Setters
}
