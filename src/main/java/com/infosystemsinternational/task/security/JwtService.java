package com.infosystemsinternational.task.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JSON Web Token (JWT) operations such as
 * generating tokens, validating tokens, and extracting information from tokens.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Extracts the personal ID (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the personal ID (subject) extracted from the token
     */
    public String extractPersonalId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from the given JWT token using the provided claims resolver function.
     *
     * @param token the JWT token
     * @param claimsResolver a function to extract the desired claim from the token's claims
     * @param <T> the type of the claim
     * @return the extracted claim
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user details and additional claims.
     *
     * @param extraClaims a map of additional claims to include in the token
     * @param employeeDetails the user details of the employee
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, EmployeeDetails employeeDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(employeeDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the given JWT token for the specified user details.
     *
     * @param token the JWT token
     * @param employeeDetails the user details of the employee
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, EmployeeDetails employeeDetails) {
        return extractPersonalId(token).equals(employeeDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extracts the roles from the given JWT token.
     *
     * @param token the JWT token
     * @return a list of roles extracted from the token
     */
    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("role");
    }

    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the given JWT token.
     *
     * @param token the JWT token
     * @return the expiration date extracted from the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Retrieves the signing key for JWT based on the secret key.
     *
     * @return the signing key
     */
    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
