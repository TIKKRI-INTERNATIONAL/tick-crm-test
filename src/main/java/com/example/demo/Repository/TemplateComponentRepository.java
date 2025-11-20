package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.DB.ComponentType;
import com.example.demo.DB.TemplateComponent;

@Repository
public interface TemplateComponentRepository extends JpaRepository<TemplateComponent, Long> {
    List<TemplateComponent> findByType(ComponentType type);
}
