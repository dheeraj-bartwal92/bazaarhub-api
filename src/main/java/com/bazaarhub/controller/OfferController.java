package com.bazaarhub.controller;

import com.bazaarhub.common.ApiResponse;
import com.bazaarhub.dto.offer.OfferRequest;
import com.bazaarhub.dto.offer.OfferResponse;
import com.bazaarhub.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OfferResponse, String>> create(@Valid @RequestBody OfferRequest request) {
        OfferResponse created = offerService.create(request);
        return ResponseEntity
                .created(URI.create("/api/offers/" + created.id()))
                .body(ApiResponse.success("Offer created", created));
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<ApiResponse<List<OfferResponse>, String>> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(offerService.getByProduct(productId)));
    }

    @GetMapping("/by-product/{productId}/buy-box")
    public ResponseEntity<ApiResponse<OfferResponse, String>> getBuyBoxWinner(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.success(offerService.getBuyBoxWinner(productId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void, String>> delete(@PathVariable Long id) {
        offerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Offer deleted", null));
    }
}