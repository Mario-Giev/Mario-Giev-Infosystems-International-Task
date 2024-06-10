package com.infosystemsinternational.task.repository;

import com.infosystemsinternational.task.entity.Directorate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository interface for Directorate entities.
 */
public interface DirectorateRepository extends JpaRepository<Directorate, Long> {

    /**
     * Finds all directorates by their active status, with pagination.
     *
     * @param active the active status of the directorates to find
     * @param pageable the pagination information
     * @return a paginated list of directorates with the specified active status
     */
    Page<Directorate> findByActive(boolean active, Pageable pageable);

    /**
     * Searches for directorates by their name or description matching the search term, with pagination.
     *
     * @param searchTerm the search term to filter directorate names and descriptions
     * @param pageable the pagination information
     * @return a paginated list of directorates matching the search term
     */
    @Query("SELECT d FROM Directorate d WHERE (LOWER(d.name) LIKE %:searchTerm%) OR (LOWER(d.description) LIKE %:searchTerm%)")
    Page<Directorate> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Finds a directorate by the ID of its director.
     *
     * @param directorId the ID of the director
     * @return an Optional containing the directorate if found, or empty if not found
     */
    @Query("SELECT d FROM Directorate d WHERE d.director.id = :directorId")
    Optional<Directorate> findByDirectorId(@Param("directorId") Long directorId);
}
