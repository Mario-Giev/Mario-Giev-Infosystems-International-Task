package com.infosystemsinternational.task.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Base entity class that includes common fields for all entities.
 */
@Data
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * The unique identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Indicates whether the entity is active.
     */
    private boolean active = true;
}
