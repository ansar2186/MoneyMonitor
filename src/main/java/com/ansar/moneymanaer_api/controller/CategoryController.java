package com.ansar.moneymanaer_api.controller;

import com.ansar.moneymanaer_api.dto.CategoryDto;
import com.ansar.moneymanaer_api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryDto));

    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCetegories() {

        List<CategoryDto> categoriesForCurrentProfile = categoryService.getCategoriesForCurrentProfile();

        return ResponseEntity.ok(categoriesForCurrentProfile);

    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDto>> getCategoryByTypeForCurrentUser(@PathVariable String type) {
        return ResponseEntity.ok(categoryService.getCategoriesByTypeForCurrentUser(type));

    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {

        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDto));

    }
}
