package com.example.demo.DB;

import java.util.List;



import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "template_components")
public class TemplateComponent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComponentType type;
    
    @Column(columnDefinition = "TEXT")
    private String text;
    
    @Column(name = "example_json")
    private String exampleJson;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "component_id")
    private List<Button> buttons;

    

    public TemplateComponent(Long id, ComponentType type, String text, String exampleJson, List<Button> buttons) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.exampleJson = exampleJson;
        this.buttons = buttons;
    }

    public TemplateComponent() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExampleJson() {
        return exampleJson;
    }

    public void setExampleJson(String exampleJson) {
        this.exampleJson = exampleJson;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }
    
    // Constructors, Getters, Setters
}
