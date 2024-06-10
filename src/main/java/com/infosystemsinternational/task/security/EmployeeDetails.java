package com.infosystemsinternational.task.security;

import com.infosystemsinternational.task.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * A class that implements the UserDetails interface for Spring Security,
 * representing an employee's details required for authentication and authorization.
 */
public class EmployeeDetails implements UserDetails {

    private final Employee employee;

    /**
     * Constructs an EmployeeDetails object with the given employee.
     *
     * @param employee the employee whose details are to be represented
     */
    public EmployeeDetails(Employee employee) {
        this.employee = employee;
    }

    /**
     * Returns the authorities granted to the employee.
     *
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + employee.getPosition().name()));
    }

    /**
     * Returns the password of the employee.
     *
     * @return the employee's password
     */
    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    /**
     * Returns the username used to authenticate the employee.
     *
     * @return the employee's personal ID
     */
    @Override
    public String getUsername() {
        return employee.getPersonalId();
    }

    /**
     * Indicates whether the employee's account has expired.
     *
     * @return true if the account is non-expired, false otherwise
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the employee is locked or unlocked.
     *
     * @return true if the account is non-locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the employee's credentials (password) has expired.
     *
     * @return true if the credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the employee is enabled or disabled.
     *
     * @return true if the employee is enabled, false otherwise
     */
    @Override
    public boolean isEnabled() {
        return employee.isActive();
    }
}
