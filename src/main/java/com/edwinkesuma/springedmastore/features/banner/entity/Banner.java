package com.edwinkesuma.springedmastore.features.banner.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "banners")
@Getter
@Setter
public class Banner extends BaseEntity {

    @Size(max = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank
    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BannerRedirectType redirectType;

    @Size(max = 255)
    private String redirectValue;

    private Instant startAt;

    private Instant endAt;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer sortOrder = 0;

    @NotNull
    @Column(nullable = false)
    private Boolean isActive = true;
}
