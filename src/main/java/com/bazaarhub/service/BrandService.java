package com.bazaarhub.service;

import com.bazaarhub.dto.brand.BrandRequest;
import com.bazaarhub.dto.brand.BrandResponse;
import com.bazaarhub.entity.Brand;
import com.bazaarhub.exception.ResourceNotFoundException;
import com.bazaarhub.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public BrandResponse create(BrandRequest request) {
        if (brandRepository.findByName(request.name()).isPresent()) {
            throw new IllegalStateException("Brand already exists: " + request.name());
        }

        Brand brand = new Brand();
        brand.setName(request.name());
        brand.setLogoUrl(request.logoUrl());
        brand.setDescription(request.description());

        Brand saved = brandRepository.save(brand);
        return toResponse(saved);
    }

    public Page<BrandResponse> getAll(Pageable pageable) {
        return brandRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public BrandResponse getById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + id));
        return toResponse(brand);
    }

    public void delete(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException("Brand not found: " + id);
        }
        brandRepository.deleteById(id);
    }

    private BrandResponse toResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getLogoUrl(),
                brand.getDescription()
        );
    }
}