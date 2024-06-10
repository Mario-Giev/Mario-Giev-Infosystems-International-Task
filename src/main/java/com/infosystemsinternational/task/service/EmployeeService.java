package com.infosystemsinternational.task.service;

import com.infosystemsinternational.task.dto.CreateEmployeeDTO;
import com.infosystemsinternational.task.util.Converter;
import com.infosystemsinternational.task.dto.EmployeeDTO;
import com.infosystemsinternational.task.entity.Employee;
import com.infosystemsinternational.task.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing employees.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final Converter converter;

    /**
     * Retrieves a paginated list of all employees, optionally filtered by active status.
     *
     * @param pageable the pagination information
     * @param activeEmployeesOnly if true, only active employees are returned
     * @return a paginated list of employees
     */
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable, boolean activeEmployeesOnly) {
        if (activeEmployeesOnly) {
            return employeeRepository.findByActive(true, pageable).map(converter::convert);
        } else {
            return employeeRepository.findAll(pageable).map(converter::convert);
        }
    }

    /**
     * Searches for employees by name or surname matching the search term.
     *
     * @param pageable the pagination information
     * @param searchTerm the search term to filter employee names and surname
     * @return a paginated list of employees matching the search term
     */
    public Page<EmployeeDTO> searchEmployees(Pageable pageable, String searchTerm) {
        return employeeRepository.findByNameOrSurname(searchTerm.toLowerCase(), pageable).map(converter::convert);
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param employeeId the ID of the employee to retrieve
     * @return an Optional containing the employee DTO if found, or empty if not found
     */
    public Optional<EmployeeDTO> getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).map(converter::convert);
    }

    /**
     * Creates a new employee.
     *
     * @param createEmployeeDTO the details of the employee to create
     * @return an Optional containing the created employee DTO, or empty if an employee with the same personal ID already exists
     */
    public Optional<EmployeeDTO> createEmployee(CreateEmployeeDTO createEmployeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findByPersonalId(createEmployeeDTO.getPersonalId());

        if (optionalEmployee.isPresent()) {
            return Optional.empty();
        }

        Employee employee = converter.convertDTO(createEmployeeDTO);
        return Optional.of(converter.convert(employeeRepository.save(employee)));
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param employeeId the ID of the employee to delete
     * @return true if the employee was deleted, false if the employee was not found
     */
    public boolean deleteEmployeeById(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isPresent()) {
            employeeRepository.deleteById(employeeId);
            return true;
        }

        return false;
    }

    /**
     * Updates the details of an existing employee.
     *
     * @param employeeId the ID of the employee to update
     * @param employeeDTO the new details of the employee
     * @return an Optional containing the updated employee DTO if the update was successful, or empty if the employee was not found
     */
    public Optional<EmployeeDTO> updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isPresent()) {
            employeeDTO.setId(employeeId);
            Employee employee = converter.convertDTO(employeeDTO);

            return Optional.of(converter.convert(employeeRepository.save(employee)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Updates the password of an existing employee.
     *
     * @param employeeId the ID of the employee to update
     * @param newPassword the new password for the employee
     * @return an Optional containing the updated employee DTO if the update was successful, or empty if the employee was not found
     */
    public Optional<EmployeeDTO> updateEmployeePassword(Long employeeId, String newPassword) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setPassword(passwordEncoder.encode(newPassword));

            return Optional.of(converter.convert(employeeRepository.save(employee)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Updates the active status of an existing employee.
     *
     * @param employeeId the ID of the employee to update
     * @param active the new active status of the employee
     * @return an Optional containing the updated employee DTO if the update was successful, or empty if the employee was not found
     */
    public Optional<EmployeeDTO> updateEmployeeStatus(Long employeeId, boolean active) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);

        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            employee.setActive(active);

            return Optional.of(converter.convert(employeeRepository.save(employee)));
        } else {
            return Optional.empty();
        }
    }
}
