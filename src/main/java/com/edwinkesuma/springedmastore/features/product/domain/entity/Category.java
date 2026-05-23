package com.edwinkesuma.springedmastore.features.product.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9-]+$")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    private String iconUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoryType type;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer sortOrder = 0;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;
}