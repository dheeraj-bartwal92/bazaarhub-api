package com.bazaarhub.controller;

import com.bazaarhub.common.ApiResponse;
import com.bazaarhub.dto.CategoryRequest;
import com.bazaarhub.dto.CategoryResponse;
import com.bazaarhub.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse, String>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse created = categoryService.create(request);
        return ResponseEntity
                .created(URI.create("/api/categories/" + created.id()))
                .body(ApiResponse.success("Category created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryResponse>, String>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse, String>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getById(id)));
    }

    @GetMapping("/top-level")
    public ResponseEntity<ApiResponse<List<CategoryResponse>, String>> getTopLevel() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getTopLevel()));
    }

    @GetMapping("/{id}/children")
    public ResponseEntity<ApiResponse<List<CategoryResponse>, String>> getChildren(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(categoryService.getChildren(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void, String>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted", null));
    }
}