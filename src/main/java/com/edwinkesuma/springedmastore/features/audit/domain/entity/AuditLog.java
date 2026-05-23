package com.edwinkesuma.springedmastore.features.audit.domain.entity;

import com.edwinkesuma.springedmastore.common.entity.BaseEntity;
import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String action;

    @Size(max = 100)
    private String entity;

    @Size(max = 100)
    private String entityId;

    @Column(columnDefinition = "jsonb")
    private String oldValue;

    @Column(columnDefinition = "jsonb")
    private String newValue;

    @Size(max = 50)
    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;
}