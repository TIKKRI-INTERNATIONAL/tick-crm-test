package com.example.demo.Config;

import java.util.List;
import java.util.Map;

import com.example.demo.DB.ComponentType;

import jakarta.validation.constraints.NotNull;

public class ComponentRequest {
    @NotNull
    private ComponentType type;
    
    private String text;
    private List<ButtonRequest> buttons;
    private Map<String, Object> example;




   public ComponentRequest(@NotNull ComponentType type, String text, List<ButtonRequest> buttons,
            Map<String, Object> example) {
        this.type = type;
        this.text = text;
        this.buttons = buttons;
        this.example = example;
    }

    public ComponentRequest() {

    }

       public void setType(ComponentType type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setButtons(List<ButtonRequest> buttons) {
        this.buttons = buttons;
    }

    public void setExample(Map<String, Object> example) {
        this.example = example;
    }

    public ComponentType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public List<ButtonRequest> getButtons() {
        return buttons;
    }

    public Map<String, Object> getExample() {
        return example;
    }

 
   
   
    
    // Getters and Setters


}
