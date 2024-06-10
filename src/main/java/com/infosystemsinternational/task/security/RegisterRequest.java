package com.infosystemsinternational.task.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing a registration request.
 * This class is used to encapsulate the data needed for a user to register.
 * It includes the user's first name, last name, personal ID, password, and age.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * The first name of the user.
     */
    private String firstName;

    /**
     * The last name of the user.
     */
    private String lastName;

    /**
     * The personal ID of the user.
     */
    private String personalId;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The age of the user.
     */
    private int age;
}
