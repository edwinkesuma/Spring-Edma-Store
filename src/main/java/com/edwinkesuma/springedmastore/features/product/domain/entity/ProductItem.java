package com.edwinkesuma.springedmastore.features.product.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "product_items")
@Getter
@Setter
public class ProductItem extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String digiflazzSku;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal basePrice;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 17, fraction = 2)
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal sellPrice;

    @Min(0)
    private Integer stock;

    @NotNull
    @Column(nullable = false)
    private Boolean isUnlimitedStock = true;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;
}