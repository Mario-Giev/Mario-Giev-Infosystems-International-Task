package com.infosystemsinternational.task.repository;

import com.infosystemsinternational.task.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for Department entities.
 */
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Finds all departments by their active status, with pagination.
     *
     * @param active the active status of the departments to find
     * @param pageable the pagination information
     * @return a paginated list of departments with the specified active status
     */
    Page<Department> findByActive(boolean active, Pageable pageable);

    /**
     * Searches for departments by their name or description matching the search term, with pagination.
     *
     * @param searchTerm the search term to filter department names and descriptions
     * @param pageable the pagination information
     * @return a paginated list of departments matching the search term
     */
    @Query("SELECT d FROM Department d WHERE (LOWER(d.name) LIKE %:searchTerm%) OR (LOWER(d.description) LIKE %:searchTerm%)")
    Page<Department> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
