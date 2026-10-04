package com.bazaarhub.service;

import com.bazaarhub.dto.seller.SellerRequest;
import com.bazaarhub.dto.seller.SellerResponse;
import com.bazaarhub.entity.Seller;
import com.bazaarhub.exception.ResourceNotFoundException;
import com.bazaarhub.repository.SellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public SellerResponse create(SellerRequest request) {
        if (sellerRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalStateException("Seller already exists with email: " + request.email());
        }

        Seller seller = new Seller();
        seller.setBusinessName(request.businessName());
        seller.setEmail(request.email());
        seller.setPhone(request.phone());

        Seller saved = sellerRepository.save(seller);
        return toResponse(saved);
    }

    public Page<SellerResponse> getAll(Pageable pageable) {
        return sellerRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public SellerResponse getById(Long id) {
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + id));
        return toResponse(seller);
    }

    public void delete(Long id) {
        if (!sellerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Seller not found: " + id);
        }
        sellerRepository.deleteById(id);
    }

    private SellerResponse toResponse(Seller seller) {
        return new SellerResponse(
                seller.getId(),
                seller.getBusinessName(),
                seller.getEmail(),
                seller.getPhone(),
                seller.getRating(),
                seller.getTotalRatings(),
                seller.isVerified(),
                seller.isActive()
        );
    }
}