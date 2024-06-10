package com.infosystemsinternational.task.config;

import com.infosystemsinternational.task.entity.Employee;
import com.infosystemsinternational.task.repository.EmployeeRepository;
import com.infosystemsinternational.task.security.EmployeeDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for defining Spring beans related to authentication and security.
 */
@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final EmployeeRepository employeeRepository;

    /**
     * Defines a UserDetailsService bean that loads user-specific data.
     *
     * @return a UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return personalId -> {
            Employee employee = employeeRepository.findByPersonalId(personalId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

            return new EmployeeDetails(employee);
        };
    }

    /**
     * Defines an AuthenticationProvider bean that uses DAO-based authentication.
     *
     * @return a DaoAuthenticationProvider implementation
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * Defines a PasswordEncoder bean that uses BCrypt hashing algorithm.
     *
     * @return a BCryptPasswordEncoder implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Defines an AuthenticationManager bean.
     *
     * @param configuration the AuthenticationConfiguration object
     * @return the AuthenticationManager implementation
     * @throws Exception if an error occurs while creating the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
