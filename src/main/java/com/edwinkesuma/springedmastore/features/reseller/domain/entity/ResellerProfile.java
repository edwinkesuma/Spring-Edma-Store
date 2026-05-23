package com.edwinkesuma.springedmastore.features.reseller.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "reseller_profiles")
@Getter
@Setter
public class ResellerProfile extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Size(max = 150)
    private String storeName;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer level = 1;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalTopup = BigDecimal.ZERO;

    private Instant approvedAt;
}
