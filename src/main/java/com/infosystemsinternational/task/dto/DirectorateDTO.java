package com.infosystemsinternational.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Directorate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectorateDTO {

    /**
     * The unique identifier for the directorate.
     */
    private Long id;

    /**
     * Indicates whether the directorate is active.
     */
    private boolean active;

    /**
     * The name of the directorate.
     */
    private String name;

    /**
     * The description of the directorate.
     */
    private String description;

    /**
     * The ID of the director of the directorate.
     */
    private Long directorId;
}
