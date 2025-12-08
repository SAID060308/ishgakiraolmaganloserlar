package org.example.petproject.controller;

import org.example.petproject.modules.repo.CategoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        try {
            return ResponseEntity.ok(categoryRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Xatolik yuz berdi: \" + e.getMessage()}");
        }
    }
}