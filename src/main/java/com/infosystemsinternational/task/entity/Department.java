package com.infosystemsinternational.task.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity representing a department within a directorate.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseEntity {

    /**
     * Name of the department.
     */
    @Column
    private String name;

    /**
     * Description of the department.
     */
    @Column
    private String description;

    /**
     * The directorate to which the department belongs.
     */
    @ManyToOne
    @JoinColumn(name = "directorate_id")
    private Directorate directorate;

    /**
     * List of employees in the department.
     */
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    /**
     * Constructs a new Department with the specified name, description, and directorate.
     *
     * @param name the name of the department
     * @param description the description of the department
     * @param directorate the directorate to which the department belongs
     */
    public Department(String name, String description, Directorate directorate) {
        this.name = name;
        this.description = description;
        this.directorate = directorate;
    }
}
