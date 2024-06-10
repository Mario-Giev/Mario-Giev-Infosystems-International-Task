package com.infosystemsinternational.task.controller;

import com.infosystemsinternational.task.dto.DirectorateDTO;
import com.infosystemsinternational.task.service.DirectorateService;
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
@RequestMapping("/api/directorates")
@Tag(name = "Directorate Controller", description = "Provides endpoints for CRUD operations")
@RequiredArgsConstructor
public class DirectorateController {

    private final DirectorateService directorateService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "List all directorates", description = "Get a paginated list of directorates. Optionally filter out the deactivated directorates.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "204", description = "No directorates found")
    })
    public ResponseEntity<Page<DirectorateDTO>> getAllDirectorates(
            @Parameter(description = "Formed by query parameters such as page, size, and sort. Page is the current page - default is 0. Size is the page size - default is 20. Sort format -> DirectorateDTO field,asc/desc") Pageable pageable,
            @Parameter(description = "Filters out the deactivated directorates") @RequestParam(defaultValue = "false") boolean activeDirectoratesOnly) {
        Page<DirectorateDTO> directorateDTOPage = directorateService.getAllDirectorates(pageable, activeDirectoratesOnly);

        return directorateDTOPage.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(directorateDTOPage);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Search directorates", description = "Search for directorates by name or description matching the search term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved directorates"),
            @ApiResponse(responseCode = "204", description = "No directorates found")
    })
    public ResponseEntity<Page<DirectorateDTO>> searchDirectorate(
            @Parameter(description = "Forms by query parameters such as page, size, and sort. Page is the current page - default is 0. Size is the page size - default is 20. Sort format: DirectorateDTO field,asc/desc") Pageable pageable,
            @Parameter(description = "Search term to filter directorate names and descriptions") @RequestParam String searchTerm) {
        Page<DirectorateDTO> directorateDTOPage = directorateService.searchDirectorate(pageable, searchTerm);

        return directorateDTOPage.isEmpty()
                ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
                : ResponseEntity.ok(directorateDTOPage);
    }

    @GetMapping("/{directorateId}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Get a directorate by ID", description = "Retrieve a directorate by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved directorate"),
            @ApiResponse(responseCode = "404", description = "Directorate not found")
    })
    public ResponseEntity<DirectorateDTO> getDirectorateById(
            @Parameter(description = "ID of the directorate to retrieve") @PathVariable Long directorateId) {
        Optional<DirectorateDTO> optionalDirectorateDTO = directorateService.getDirectorateById(directorateId);

        return optionalDirectorateDTO.isPresent()
                ? ResponseEntity.ok(optionalDirectorateDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Create a new directorate", description = "Create a new directorate with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created directorate"),
            @ApiResponse(responseCode = "409", description = "Conflict - directorate already exists")
    })
    public ResponseEntity<DirectorateDTO> createDirectorate(
            @Parameter(description = "Details of the directorate to create") @RequestBody DirectorateDTO directorateDTO) {
        Optional<DirectorateDTO> optionalDirectorateDTO = directorateService.createDirectorate(directorateDTO);

        return optionalDirectorateDTO.isPresent()
                ? ResponseEntity.status(HttpStatus.CREATED).body(optionalDirectorateDTO.get())
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{directorateId}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Delete a directorate by ID", description = "Delete a directorate by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted directorate"),
            @ApiResponse(responseCode = "404", description = "Directorate not found")
    })
    public ResponseEntity<Void> deleteDirectorateById(
            @Parameter(description = "ID of the directorate to delete") @PathVariable Long directorateId) {
        return directorateService.deleteDirectorateById(directorateId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/{directorateId}")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Update a directorate", description = "Update the details of an existing directorate.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated directorate"),
            @ApiResponse(responseCode = "404", description = "Directorate not found")
    })
    public ResponseEntity<DirectorateDTO> updateDirectorate(
            @Parameter(description = "ID of the directorate to update") @PathVariable Long directorateId,
            @Parameter(description = "Updated directorate details") @RequestBody DirectorateDTO directorateDTO) {
        Optional<DirectorateDTO> optionalDirectorateDTO = directorateService.updateDirectorate(directorateId, directorateDTO);

        return optionalDirectorateDTO.isPresent()
                ? ResponseEntity.ok(optionalDirectorateDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{directorateId}/activate")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Activate a directorate", description = "Activate a directorate by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully activated directorate"),
            @ApiResponse(responseCode = "404", description = "Directorate not found")
    })
    public ResponseEntity<DirectorateDTO> activateDirectorate(
            @Parameter(description = "ID of the directorate to activate") @PathVariable Long directorateId) {
        Optional<DirectorateDTO> optionalDirectorateDTO = directorateService.updateDirectorateStatus(directorateId, true);

        return optionalDirectorateDTO.isPresent()
                ? ResponseEntity.ok(optionalDirectorateDTO.get())
                : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{directorateId}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_DIRECTORATE_DIRECTOR')")
    @Operation(summary = "Deactivate a directorate", description = "Deactivate a directorate by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deactivated directorate"),
            @ApiResponse(responseCode = "404", description = "Directorate not found")
    })
    public ResponseEntity<DirectorateDTO> deactivateDirectorate(
            @Parameter(description = "ID of the directorate to deactivate") @PathVariable Long directorateId) {
        Optional<DirectorateDTO> optionalDirectorateDTO = directorateService.updateDirectorateStatus(directorateId, false);

        return optionalDirectorateDTO.isPresent()
                ? ResponseEntity.ok(optionalDirectorateDTO.get())
                : ResponseEntity.notFound().build();
    }
}
