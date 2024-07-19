package com.ra.controller;

import com.ra.model.dto.res.CategoryWithProductsDTO;
import com.ra.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final ICategoryService categoryService;
    @GetMapping("/category/{id}")
    public ResponseEntity<?> viewCategory(@PathVariable Long id) {
        Optional<CategoryWithProductsDTO> categoryWithProducts = categoryService.findCategoryWithProducts(id);
        if (categoryWithProducts.isEmpty()) {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }

        CategoryWithProductsDTO dto = categoryWithProducts.get();
        if (dto.getProducts().isEmpty()) {
            dto.setMessage("No products found for this category.");
        }
        return ResponseEntity.ok(dto);
    }
}
