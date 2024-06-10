package com.infosystemsinternational.task.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A class representing an authentication response.
 * This class is used to encapsulate the data returned to the user upon successful authentication.
 * It includes a token that can be used for authenticated requests.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    /**
     * The token provided upon successful authentication.
     */
    private String token;
}
