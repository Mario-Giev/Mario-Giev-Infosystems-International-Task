package com.infosystemsinternational.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Employee.
 * Used for Employee creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeDTO {


    /**
     * Indicates whether the employee is active.
     */
    private boolean active;

    /**
     * The first name of the employee.
     */
    private String name;

    /**
     * The surname of the employee.
     */
    private String surname;

    /**
     * The personal ID of the employee.
     */
    private String personalId;

    /**
     * The password of the employee
     */
    String password;
    /**
     * The age of the employee.
     */
    private int age;

    /**
     * The position of the employee.
     */
    private String position;

    /**
     * The ID of the department to which the employee belongs.
     */
    private Long departmentId;
}
