package com.infosystemsinternational.task.controller;

import com.infosystemsinternational.task.dto.CreateEmployeeDTO;
import com.infosystemsinternational.task.dto.EmployeeDTO;
import com.infosystemsinternational.task.service.EmployeeService;
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
@RequestMapping("/api/employees")
@Tag(name = "Employee Controller", description = "Provides endpoints for CRUD operations")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "List all employees", description = "Get a paginated list of employees. Optionally filter out the deactivated employees.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "204", description = "No employees found")
    })
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
            @Parameter(description = "Forms by query parameters such as page, size, and sort. Page is the current page - default is 0. Size is the page size - default is 20. Sort format: EmployeeDTO field,asc/desc") Pageable pageable,
            @Parameter(description = "Filters out the deactivated employees") @RequestParam(defaultValue = "false") boolean activeEmployeesOnly) {
        Page<EmployeeDTO> employeeDTOPage = employeeService.getAllEmployees(pageable, activeEmployeesOnly);

        return employeeDTOPage.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(employeeDTOPage);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Search employees", description = "Search for employees by name or surname matching the search term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employees"),
            @ApiResponse(responseCode = "204", description = "No employees found")
    })
    public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
            @Parameter(description = "Forms by query parameters such as page, size, and sort. Page is the current page - default is 0. Size is the page size - default is 20. Sort format: EmployeeDTO field,asc/desc") Pageable pageable,
            @Parameter(description = "Search term to filter employee names and surnames") @RequestParam String searchTerm) {
        Page<EmployeeDTO> employeeDTOPage = employeeService.searchEmployees(pageable, searchTerm);

        return employeeDTOPage.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(employeeDTOPage);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Get an employee by ID", description = "Retrieve an employee by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @Parameter(description = "ID of the employee to retrieve") @PathVariable Long employeeId) {
        Optional<EmployeeDTO> optionalEmployeeDTO = employeeService.getEmployeeById(employeeId);

        return optionalEmployeeDTO.isPresent()
                ? ResponseEntity.ok(optionalEmployeeDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Create a new employee", description = "Create a new employee with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created employee"),
            @ApiResponse(responseCode = "409", description = "Conflict - employee already exists")
    })
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Parameter(description = "Details of the employee to create") @RequestBody CreateEmployeeDTO createEmployeeDTO) {
        Optional<EmployeeDTO> optionalEmployeeDTO = employeeService.createEmployee(createEmployeeDTO);

        return optionalEmployeeDTO.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(optionalEmployeeDTO.get())
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Delete an employee by ID", description = "Delete an employee by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<Void> deleteEmployeeById(
            @Parameter(description = "ID of the employee to delete") @PathVariable Long employeeId) {
        return employeeService.deleteEmployeeById(employeeId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Update an employee", description = "Update the details of an existing employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @Parameter(description = "ID of the employee to update") @PathVariable Long employeeId,
            @Parameter(description = "Updated employee details") @RequestBody EmployeeDTO employeeDTO) {
        Optional<EmployeeDTO> optionalEmployeeDTO = employeeService.updateEmployee(employeeId, employeeDTO);

        return optionalEmployeeDTO.isPresent()
                ? ResponseEntity.ok(optionalEmployeeDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{employeeId}/password")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Update an employee's password", description = "Update the password of an existing employee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated employee's password"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDTO> updateEmployeePassword(
            @Parameter(description = "ID of the employee to update") @PathVariable Long employeeId,
            @Parameter(description = "New password") @RequestBody String newPassword) {
        Optional<EmployeeDTO> optionalEmployeeDTO = employeeService.updateEmployeePassword(employeeId, newPassword);

        return optionalEmployeeDTO.isPresent()
                ? ResponseEntity.ok(optionalEmployeeDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{employeeId}/activate")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Activate an employee", description = "Activate an employee by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully activated employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDTO> activateEmployee(
            @Parameter(description = "ID of the employee to activate") @PathVariable Long employeeId) {
        Optional<EmployeeDTO> optionalEmployeeDTO = employeeService.updateEmployeeStatus(employeeId, true);

        return optionalEmployeeDTO.isPresent()
                ? ResponseEntity.ok(optionalEmployeeDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{employeeId}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_DEPARTMENT_HEAD', 'ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Deactivate an employee", description = "Deactivate an employee by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deactivated employee"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeDTO> deactivateEmployee(
            @Parameter(description = "ID of the employee to deactivate") @PathVariable Long employeeId) {
        Optional<EmployeeDTO> optionalEmployeeDTO = employeeService.updateEmployeeStatus(employeeId, false);

        return optionalEmployeeDTO.isPresent()
                ? ResponseEntity.ok(optionalEmployeeDTO.get())
                : ResponseEntity.notFound().build();
    }
}
