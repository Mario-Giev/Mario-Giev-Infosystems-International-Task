package com.infosystemsinternational.task.entity;

import com.infosystemsinternational.task.enums.Position;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity representing a directorate within an organization.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Directorate extends BaseEntity {

    /**
     * Name of the directorate.
     */
    @Column
    private String name;

    /**
     * Description of the directorate.
     */
    @Column
    private String description;

    /**
     * The director of the directorate.
     */
    @OneToOne()
    @JoinColumn(name = "director_id", unique = true)
    private Employee director;

    /**
     * List of departments under this directorate.
     */
    @OneToMany(mappedBy = "directorate")
    private List<Department> departments;

    /**
     * Constructs a new Directorate with the specified name and description.
     *
     * @param name the name of the directorate
     * @param description the description of the directorate
     */
    public Directorate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Sets the director of the directorate. Ensures the director has the appropriate position.
     *
     * @param director the employee to set as director
     */
    public void setDirector(Employee director) {
        if (director.getPosition() != null && director.getPosition() == Position.DIRECTORATE_DIRECTOR) {
            this.director = director;
        }
    }
}
