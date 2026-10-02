package com.bazaarhub.service;

import com.bazaarhub.dto.CategoryRequest;
import com.bazaarhub.dto.CategoryResponse;
import com.bazaarhub.entity.Category;
import com.bazaarhub.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.findByName(request.name()).isPresent()) {
            throw new RuntimeException("Category already exists: " + request.name());
        }

        Category category = new Category();
        category.setName(request.name());
        category.setSlug(toSlug(request.name()));
        category.setDescription(request.description());
        category.setImageUrl(request.imageUrl());

        if (request.parentId() != null) {
            Category parent = categoryRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found: " + request.parentId()));
            category.setParent(parent);
        }

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    public Page<CategoryResponse> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public CategoryResponse getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        return toResponse(category);
    }

    public List<CategoryResponse> getTopLevel() {
        return categoryRepository.findByParentIsNull().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CategoryResponse> getChildren(Long parentId) {
        return categoryRepository.findByParentId(parentId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {
        Category parent = category.getParent();
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription(),
                category.getImageUrl(),
                category.isActive(),
                parent != null ? parent.getId() : null,
                parent != null ? parent.getName() : null
        );
    }

    private String toSlug(String name) {
        return name.trim().toLowerCase().replaceAll("[^a-z0-9]+", "-");
    }
}
