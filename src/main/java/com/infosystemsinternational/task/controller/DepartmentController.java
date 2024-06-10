package com.infosystemsinternational.task.controller;

import com.infosystemsinternational.task.dto.DepartmentDTO;
import com.infosystemsinternational.task.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
@Tag(name = "Department Controller", description = "Provides endpoints for CRUD operations")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "List all departments", description = "Get a paginated list of departments. Optionally filter out the deactivated departments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "204", description = "No departments found")
    })
    public ResponseEntity<Page<DepartmentDTO>> getAllDepartments(
            @Parameter(description = "Formed by query parameters such as page, size and sort. Page is the current page - deafault is 0. Size is the page size - default is 20. Sort format: DepartmentDTO field,asc/desc") Pageable pageable,
            @Parameter(description = "Filters out the deactivated departments") @RequestParam(defaultValue = "false") boolean activeDepartmentsOnly) {
        Page<DepartmentDTO> departmentDTOPage = departmentService.getAllDepartments(pageable, activeDepartmentsOnly);

        return departmentDTOPage.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(departmentDTOPage);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Search Departments", description = "Search for departments by name or description matching the search term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved departments"),
            @ApiResponse(responseCode = "204", description = "No departments found")
    })
    public ResponseEntity<Page<DepartmentDTO>> searchDepartments(
            @Parameter(description = "Forms by query parameters such as page, size and sort. Page is the current page - deafault is 0. Size is the page size - default is 20. Sort format: DepartmentDTO field,asc/desc") Pageable pageable,
            @Parameter(description = "Search term to filter department names and descriptions") @RequestParam String searchTerm) {
        Page<DepartmentDTO> departmentDTOPage = departmentService.searchDepartments(pageable, searchTerm);

        return departmentDTOPage.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(departmentDTOPage);
    }

    @GetMapping("/{departmentId}")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Get a department by ID", description = "Retrieve a department by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved department"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<DepartmentDTO> getDepartmentById(
            @Parameter(description = "ID of the department to retrieve") @PathVariable Long departmentId) {
        Optional<DepartmentDTO> optionalDepartmentDTO = departmentService.getDepartmentById(departmentId);

        return optionalDepartmentDTO.isPresent()
                ? ResponseEntity.ok(optionalDepartmentDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Create a new department", description = "Create a new department with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created department"),
            @ApiResponse(responseCode = "409", description = "Conflict - department already exists")
    })
    public ResponseEntity<DepartmentDTO> createDepartment(
            @Parameter(description = "Details of the department to create") @RequestBody DepartmentDTO departmentDTO) {
        Optional<DepartmentDTO> optionalDepartmentDTO = departmentService.createDepartment(departmentDTO);

        return optionalDepartmentDTO.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(optionalDepartmentDTO.get())
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{departmentId}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Delete a department by ID", description = "Delete a department by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted department"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<Void> deleteDepartmentById(
            @Parameter(description = "ID of the department to delete") @PathVariable Long departmentId) {
        return departmentService.deleteDepartmentById(departmentId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{departmentId}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Update a department", description = "Update the details of an existing department.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated department"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<DepartmentDTO> updateDepartment(
            @Parameter(description = "ID of the department to update") @PathVariable Long departmentId,
            @Parameter(description = "Updated department details") @RequestBody DepartmentDTO departmentDTO) {
        Optional<DepartmentDTO> optionalDepartmentDTO = departmentService.updateDepartment(departmentId, departmentDTO);

        return optionalDepartmentDTO.isPresent()
                ? ResponseEntity.ok(optionalDepartmentDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{departmentId}/activate")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Activate a department", description = "Activate a department by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully activated department"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<DepartmentDTO> activateDepartment(
            @Parameter(description = "ID of the department to activate") @PathVariable Long departmentId) {
        Optional<DepartmentDTO> optionalDepartmentDTO = departmentService.updateDepartmentStatus(departmentId, true);

        return optionalDepartmentDTO.isPresent()
                ? ResponseEntity.ok(optionalDepartmentDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{departmentId}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Deactivate a department", description = "Deactivate a department by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deactivated department"),
            @ApiResponse(responseCode = "404", description = "Department not found")
    })
    public ResponseEntity<DepartmentDTO> deactivateDepartment(
            @Parameter(description = "ID of the department to deactivate") @PathVariable Long departmentId) {
        Optional<DepartmentDTO> optionalDepartmentDTO = departmentService.updateDepartmentStatus(departmentId, false);

        return optionalDepartmentDTO.isPresent()
                ? ResponseEntity.ok(optionalDepartmentDTO.get())
                : ResponseEntity.notFound().build();
    }
}
