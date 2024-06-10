package com.infosystemsinternational.task.entity;

import com.infosystemsinternational.task.enums.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing an employee.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Employee extends BaseEntity {

    /**
     * Name of the employee.
     */
    @Column
    private String name;

    /**
     * Surname of the employee.
     */
    @Column
    private String surname;

    /**
     * Personal ID of the employee.
     */
    @Column(nullable = false, unique = true)
    private String personalId;

    /**
     * Password of the employee.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Age of the employee.
     */
    @Column
    private int age;

    /**
     * Position of the employee.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    /**
     * Department to which the employee belongs.
     */
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
