package com.infosystemsinternational.task.service;

import com.infosystemsinternational.task.util.Converter;
import com.infosystemsinternational.task.dto.DirectorateDTO;
import com.infosystemsinternational.task.entity.Directorate;
import com.infosystemsinternational.task.repository.DirectorateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service class for managing directorates.
 */
@Service
@RequiredArgsConstructor
public class DirectorateService {

    private final DirectorateRepository directorateRepository;
    private final Converter converter;

    /**
     * Retrieves a paginated list of all directorates, optionally filtered by active status.
     *
     * @param pageable the pagination information
     * @param activeDirectoratesOnly if true, only active directorates are returned
     * @return a paginated list of directorates
     */
    public Page<DirectorateDTO> getAllDirectorates(Pageable pageable, boolean activeDirectoratesOnly) {
        if (activeDirectoratesOnly) {
            return directorateRepository.findByActive(true, pageable).map(converter::convert);
        }
        return directorateRepository.findAll(pageable).map(converter::convert);
    }

    /**
     * Searches for directorates by name or description matching the search term.
     *
     * @param pageable the pagination information
     * @param searchTerm the search term to filter directorate names and descriptions
     * @return a paginated list of directorates matching the search term
     */
    public Page<DirectorateDTO> searchDirectorate(Pageable pageable, String searchTerm) {
        return directorateRepository.findBySearchTerm(searchTerm.toLowerCase(), pageable).map(converter::convert);
    }

    /**
     * Retrieves a directorate by its ID.
     *
     * @param directorateId the ID of the directorate to retrieve
     * @return an Optional containing the directorate DTO if found, or empty if not found
     */
    public Optional<DirectorateDTO> getDirectorateById(Long directorateId) {
        return directorateRepository.findById(directorateId).map(converter::convert);
    }

    /**
     * Creates a new directorate.
     *
     * @param directorateDTO the details of the directorate to create
     * @return an Optional containing the created directorate DTO, or empty if a directorate with the same director ID already exists
     */
    public Optional<DirectorateDTO> createDirectorate(DirectorateDTO directorateDTO) {
        Optional<Directorate> optionalDirectorate = directorateRepository.findByDirectorId(directorateDTO.getDirectorId());

        if (optionalDirectorate.isEmpty()) {
            Directorate directorate = converter.convertDTO(directorateDTO);
            return Optional.of(converter.convert(directorateRepository.save(directorate)));
        }

        return Optional.empty();
    }

    /**
     * Deletes a directorate by its ID.
     *
     * @param directorateId the ID of the directorate to delete
     * @return true if the directorate was deleted, false if the directorate was not found
     */
    public boolean deleteDirectorateById(Long directorateId) {
        Optional<Directorate> optionalDirectorate = directorateRepository.findById(directorateId);

        if (optionalDirectorate.isPresent()) {
            Directorate directorate = optionalDirectorate.get();

            if (directorate.getDepartments() != null && !directorate.getDepartments().isEmpty()) {
                directorate.getDepartments().forEach(d -> d.setDirectorate(null));
            }

            directorateRepository.deleteById(directorateId);
            return true;
        }

        return false;
    }

    /**
     * Updates the details of an existing directorate.
     *
     * @param directorateId the ID of the directorate to update
     * @param directorateDTO the new details of the directorate
     * @return an Optional containing the updated directorate DTO if the update was successful, or empty if the directorate was not found
     */
    public Optional<DirectorateDTO> updateDirectorate(Long directorateId, DirectorateDTO directorateDTO) {
        Optional<Directorate> optionalDirectorate = directorateRepository.findById(directorateId);

        if (optionalDirectorate.isPresent()) {
            directorateDTO.setId(directorateId);
            Directorate directorate = converter.convertDTO(directorateDTO);
            return Optional.of(converter.convert(directorateRepository.save(directorate)));
        }

        return Optional.empty();
    }

    /**
     * Updates the active status of an existing directorate.
     *
     * @param directorateId the ID of the directorate to update
     * @param active the new active status of the directorate
     * @return an Optional containing the updated directorate DTO if the update was successful, or empty if the directorate was not found
     */
    public Optional<DirectorateDTO> updateDirectorateStatus(Long directorateId, boolean active) {
        Optional<Directorate> optionalDirectorate = directorateRepository.findById(directorateId);

        if (optionalDirectorate.isPresent()) {
            Directorate directorate = optionalDirectorate.get();
            directorate.setActive(active);
            return Optional.of(converter.convert(directorateRepository.save(directorate)));
        }

        return Optional.empty();
    }
}
