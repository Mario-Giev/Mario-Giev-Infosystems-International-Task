package com.infosystemsinternational.task.repository;

import com.infosystemsinternational.task.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for Employee entities.
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their personal ID.
     *
     * @param personalId the personal ID of the employee
     * @return an Optional containing the employee if found, or empty if not found
     */
    Optional<Employee> findByPersonalId(String personalId);

    /**
     * Finds all employees by their active status, with pagination.
     *
     * @param active the active status of the employees to find
     * @param pageable the pagination information
     * @return a paginated list of employees with the specified active status
     */
    Page<Employee> findByActive(boolean active, Pageable pageable);

    /**
     * Searches for employees by their name or surname matching the search term, with pagination.
     *
     * @param searchTerm the search term to filter employee names and surnames
     * @param pageable the pagination information
     * @return a paginated list of employees matching the search term
     */
    @Query("SELECT e FROM Employee e WHERE (LOWER(e.name) LIKE %:searchTerm%) OR (LOWER(e.surname) LIKE %:searchTerm%)")
    Page<Employee> findByNameOrSurname(@Param("searchTerm") String searchTerm, Pageable pageable);
}
