package com.infosystemsinternational.task.security;

import com.infosystemsinternational.task.entity.Employee;
import com.infosystemsinternational.task.enums.Position;
import com.infosystemsinternational.task.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling authentication and registration operations.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new employee and returns an authentication response containing a JWT.
     *
     * @param registerRequest the request containing the registration details
     * @return an AuthenticationResponse containing the JWT
     */
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        Employee employee = new Employee(
                registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getPersonalId(),
                passwordEncoder.encode(registerRequest.getPassword()), registerRequest.getAge(), Position.EMPLOYEE, null);

        employeeRepository.save(employee);

        String jwt = jwtService.generateToken(Map.of("role", getRole(employee)), new EmployeeDetails(employee));
        return AuthenticationResponse.builder().token(jwt).build();
    }

    /**
     * Authenticates an employee and returns an authentication response containing a JWT.
     *
     * @param authenticationRequest the request containing the authentication details
     * @return an AuthenticationResponse containing the JWT
     */
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getPersonalId(), authenticationRequest.getPassword()));

        Employee employee = employeeRepository.findByPersonalId(authenticationRequest.getPersonalId()).orElseThrow();

        String jwt = jwtService.generateToken(Map.of("role", getRole(employee)), new EmployeeDetails(employee));
        return AuthenticationResponse.builder().token(jwt).build();
    }

    /**
     * Determines the roles for the given employee based on their position.
     *
     * @param employee the employee whose roles are to be determined
     * @return a list of roles for the employee
     */
    private List<String> getRole(Employee employee) {
        return switch (employee.getPosition()) {
            case DIRECTORATE_DIRECTOR -> Arrays.stream(Position.values()).map(p -> "ROLE_" + p.name()).toList();
            case DEPARTMENT_HEAD -> Arrays.stream(Position.values()).filter(p -> p != Position.DIRECTORATE_DIRECTOR).map(p -> "ROLE_" + p.name()).toList();
            default -> List.of("ROLE_" + employee.getPosition().name());
        };
    }
}
