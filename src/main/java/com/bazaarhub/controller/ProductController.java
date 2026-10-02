package com.bazaarhub.controller;

import com.bazaarhub.common.ApiResponse;
import com.bazaarhub.dto.product.ProductRequest;
import com.bazaarhub.dto.product.ProductResponse;
import com.bazaarhub.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.net.URI;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    public ResponseEntity<ApiResponse<ProductResponse, String>> create(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + created.id()))
                .body(ApiResponse.success("Product created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>, String>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse,String>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getById(id)));
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>, String>> getByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getByCategory(categoryId, pageable)));
    }

    @GetMapping("/by-brand/{brandId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>,String>> getByBrandId(@PathVariable Long brandId, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(productService.getByBrand(brandId, pageable)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void, String>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted", null));
    }
}
