package com.infosystemsinternational.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Department.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {

    /**
     * The unique identifier for the department.
     */
    private Long id;

    /**
     * Indicates whether the department is active.
     */
    private boolean active;

    /**
     * The name of the department.
     */
    private String name;

    /**
     * The description of the department.
     */
    private String description;

    /**
     * The ID of the directorate to which the department belongs.
     */
    private Long directorateId;
}
