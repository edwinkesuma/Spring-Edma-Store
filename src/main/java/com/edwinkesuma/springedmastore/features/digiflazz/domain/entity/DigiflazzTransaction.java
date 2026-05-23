package com.edwinkesuma.springedmastore.features.digiflazz.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.digiflazz.domain.enums.DigiflazzTransactionStatus;
import com.edwinkesuma.springedmastore.features.order.domain.entity.OrderItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "digiflazz_transactions")
@Getter
@Setter
public class DigiflazzTransaction extends BaseEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false, unique = true)
    private OrderItem orderItem;

    @Size(max = 100)
    private String refId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String buyerSkuCode;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String customerNo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DigiflazzTransactionStatus status;

    @Size(max = 255)
    private String sn;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(columnDefinition = "jsonb")
    private String rawResponse;

    private Instant processedAt;
}
