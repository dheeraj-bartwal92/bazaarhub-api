package com.bazaarhub.controller;

import com.bazaarhub.common.ApiResponse;
import com.bazaarhub.dto.seller.SellerRequest;
import com.bazaarhub.dto.seller.SellerResponse;
import com.bazaarhub.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SellerResponse, String>> create(@Valid @RequestBody SellerRequest request) {
        SellerResponse created = sellerService.create(request);
        return ResponseEntity
                .created(URI.create("/api/sellers/" + created.id()))
                .body(ApiResponse.success("Seller created", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SellerResponse>, String>> getAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(sellerService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SellerResponse, String>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(sellerService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void, String>> delete(@PathVariable Long id) {
        sellerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Seller deleted", null));
    }
}