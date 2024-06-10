package com.infosystemsinternational.task.enums;

/**
 * Enum representing different positions for employee.
 */
public enum Position {
    /**
     * Director of the Directorate.
     */
    DIRECTORATE_DIRECTOR("Director of the Directorate"),

    /**
     * Head of Department.
     */
    DEPARTMENT_HEAD("Head of Department"),

    /**
     * Employee.
     */
    EMPLOYEE("Employee");

    private final String label;

    /**
     * Constructor for Position enum.
     *
     * @param label the label for the position
     */
    Position(String label) {
        this.label = label;
    }

    /**
     * Gets the label of the position.
     *
     * @return the label of the position
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the Position enum constant from the label.
     *
     * @param label the label of the position
     * @return the Position enum constant
     * @throws IllegalArgumentException if no matching Position is found
     */
    public static Position fromLabel(String label) {
        for (Position position : Position.values()) {
            if (position.getLabel().equalsIgnoreCase(label)) {
                return position;
            }
        }
        throw new IllegalArgumentException("No position with label " + label + " found.");
    }
}
