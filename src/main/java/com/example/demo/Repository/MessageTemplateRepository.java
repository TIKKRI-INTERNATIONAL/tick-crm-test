package com.example.demo.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.DB.MessageTemplate;
import com.example.demo.DB.TemplateCategory;

@Repository
public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
    Optional<MessageTemplate> findByName(String name);
    List<MessageTemplate> findByCategory(TemplateCategory category);
    List<MessageTemplate> findByIsActiveTrue();
}
