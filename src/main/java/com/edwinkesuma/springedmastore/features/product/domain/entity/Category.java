package com.edwinkesuma.springedmastore.features.product.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.product.domain.enums.CategoryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    private String iconUrl;

    @Column(length = 255)
    private String iconPublicId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoryType type;

    @Column(nullable = false)
    private Integer sortOrder = 0;

    @Column(nullable = false)
    private Boolean isActive = true;
}