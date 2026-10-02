package com.bazaarhub.service;

import com.bazaarhub.dto.product.ProductRequest;
import com.bazaarhub.dto.product.ProductResponse;
import com.bazaarhub.entity.Brand;
import com.bazaarhub.entity.Category;
import com.bazaarhub.entity.Product;
import com.bazaarhub.exception.ResourceNotFoundException;
import com.bazaarhub.repository.BrandRepository;
import com.bazaarhub.repository.CategoryRepository;
import com.bazaarhub.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository,
                          BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    public ProductResponse create(ProductRequest request) {
        if (request.sku() != null && productRepository.findBySku(request.sku()).isPresent()) {
            throw new IllegalStateException("Product with SKU already exists: " + request.sku());
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));

        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setSku(request.sku());
        product.setImageUrl(request.imageUrl());
        product.setCategory(category);

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found: " + request.brandId()));
            product.setBrand(brand);
        }

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    public Page<ProductResponse> getAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return toResponse(product);
    }

    public Page<ProductResponse> getByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::toResponse);
    }

    public Page<ProductResponse> getByBrand(Long brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable)
                .map(this::toResponse);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        Brand brand = product.getBrand();
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                brand != null ? brand.getName() : null,
                product.getSku(),
                product.getImageUrl(),
                product.isActive(),
                product.getCategory().getName()
        );
    }
}