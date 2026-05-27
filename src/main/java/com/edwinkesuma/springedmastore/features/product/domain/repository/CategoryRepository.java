package com.edwinkesuma.springedmastore.features.product.domain.repository;

import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, UUID categoryId);
}
