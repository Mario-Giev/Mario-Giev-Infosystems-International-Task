package com.infosystemsinternational.task.service;

import com.infosystemsinternational.task.util.Converter;
import com.infosystemsinternational.task.dto.DepartmentDTO;
import com.infosystemsinternational.task.entity.Department;
import com.infosystemsinternational.task.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing departments.
 */
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final Converter converter;

    /**
     * Retrieves a paginated list of all departments, optionally filtered by active status.
     *
     * @param pageable the pagination information
     * @param activeDepartmentsOnly if true, only active departments are returned
     * @return a paginated list of departments
     */
    public Page<DepartmentDTO> getAllDepartments(Pageable pageable, boolean activeDepartmentsOnly) {
        if (activeDepartmentsOnly) {
            return departmentRepository.findByActive(true, pageable).map(converter::convert);
        } else {
            return departmentRepository.findAll(pageable).map(converter::convert);
        }
    }

    /**
     * Searches for departments by name or description matching the search term.
     *
     * @param pageable the pagination information
     * @param searchTerm the search term to filter department names and descriptions
     * @return a paginated list of departments matching the search term
     */
    public Page<DepartmentDTO> searchDepartments(Pageable pageable, String searchTerm) {
        return departmentRepository.findBySearchTerm(searchTerm.toLowerCase(), pageable).map(converter::convert);
    }

    /**
     * Retrieves a department by its ID.
     *
     * @param departmentId the ID of the department to retrieve
     * @return an Optional containing the department DTO if found, or empty if not found
     */
    public Optional<DepartmentDTO> getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).map(converter::convert);
    }

    /**
     * Creates a new department.
     *
     * @param departmentDTO the details of the department to create
     * @return an Optional containing the created department DTO
     */
    public Optional<DepartmentDTO> createDepartment(DepartmentDTO departmentDTO) {
        Department department = converter.convertDTO(departmentDTO);
        return Optional.of(converter.convert(departmentRepository.save(department)));
    }

    /**
     * Deletes a department by its ID.
     *
     * @param departmentId the ID of the department to delete
     * @return true if the department was deleted, false if the department was not found
     */
    public boolean deleteDepartmentById(Long departmentId) {
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);

        if (optionalDepartment.isPresent()) {
            departmentRepository.deleteById(departmentId);
            return true;
        }

        return false;
    }

    /**
     * Updates the details of an existing department.
     *
     * @param departmentId the ID of the department to update
     * @param departmentDTO the new details of the department
     * @return an Optional containing the updated department DTO if the update was successful, or empty if the department was not found
     */
    public Optional<DepartmentDTO> updateDepartment(Long departmentId, DepartmentDTO departmentDTO) {
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);

        if (optionalDepartment.isPresent()) {
            departmentDTO.setId(departmentId);
            Department department = converter.convertDTO(departmentDTO);
            return Optional.of(converter.convert(departmentRepository.save(department)));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Updates the active status of an existing department.
     *
     * @param departmentId the ID of the department to update
     * @param active the new active status of the department
     * @return an Optional containing the updated department DTO if the update was successful, or empty if the department was not found
     */
    public Optional<DepartmentDTO> updateDepartmentStatus(Long departmentId, boolean active) {
        Optional<Department> optionalDepartment = departmentRepository.findById(departmentId);

        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            department.setActive(active);
            return Optional.of(converter.convert(departmentRepository.save(department)));
        } else {
            return Optional.empty();
        }
    }
}
