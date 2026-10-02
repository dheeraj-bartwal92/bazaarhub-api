package com.bazaarhub.controller;

import com.bazaarhub.common.ApiResponse;
import com.bazaarhub.dto.brand.BrandRequest;
import com.bazaarhub.dto.brand.BrandResponse;
import com.bazaarhub.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponse, String>> create(@Valid @RequestBody BrandRequest request) {
        BrandResponse created = brandService.create(request);
        return ResponseEntity
                .created(URI.create("/api/brands/" + created.id()))
                .body(ApiResponse.success("Brand created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BrandResponse>, String>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(brandService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponse, String>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(brandService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void, String>> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Brand deleted", null));
    }
}