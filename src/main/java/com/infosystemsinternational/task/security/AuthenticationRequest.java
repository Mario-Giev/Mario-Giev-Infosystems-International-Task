package com.infosystemsinternational.task.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing an authentication request.
 * This class is used to encapsulate the data needed for a user to authenticate.
 * It includes the user's personal ID and password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    /**
     * The personal ID of the user.
     */
    private String personalId;

    /**
     * The password of the user.
     */
    private String password;
}
