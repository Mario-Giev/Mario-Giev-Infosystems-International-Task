package com.infosystemsinternational.task.service;

import com.infosystemsinternational.task.dto.DirectorateDTO;
import com.infosystemsinternational.task.entity.Directorate;
import com.infosystemsinternational.task.repository.DirectorateRepository;
import com.infosystemsinternational.task.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DirectorateServiceTest {

    @Mock
    private DirectorateRepository directorateRepository;

    @Mock
    private Converter converter;

    @InjectMocks
    private DirectorateService directorateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenActiveDirectoratesOnly_whenGetAllDirectorates_thenReturnActiveDirectorates() {
        Pageable pageable = Pageable.unpaged();
        Directorate directorate = new Directorate();
        directorate.setActive(true);
        Page<Directorate> directoratePage = new PageImpl<>(Arrays.asList(directorate));
        when(directorateRepository.findByActive(true, pageable)).thenReturn(directoratePage);
        DirectorateDTO directorateDTO = new DirectorateDTO();
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Page<DirectorateDTO> result = directorateService.getAllDirectorates(pageable, true);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(directorateRepository, times(1)).findByActive(true, pageable);
    }

    @Test
    void givenAllDirectorates_whenGetAllDirectorates_thenReturnAllDirectorates() {
        Pageable pageable = Pageable.unpaged();
        Directorate directorate = new Directorate();
        Page<Directorate> directoratePage = new PageImpl<>(Arrays.asList(directorate));
        when(directorateRepository.findAll(pageable)).thenReturn(directoratePage);
        DirectorateDTO directorateDTO = new DirectorateDTO();
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Page<DirectorateDTO> result = directorateService.getAllDirectorates(pageable, false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(directorateRepository, times(1)).findAll(pageable);
    }

    @Test
    void givenSearchTerm_whenSearchDirectorate_thenReturnMatchingDirectorates() {
        Pageable pageable = Pageable.unpaged();
        String searchTerm = "Finance";
        Directorate directorate = new Directorate();
        Page<Directorate> directoratePage = new PageImpl<>(Arrays.asList(directorate));
        when(directorateRepository.findBySearchTerm(searchTerm.toLowerCase(), pageable)).thenReturn(directoratePage);
        DirectorateDTO directorateDTO = new DirectorateDTO();
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Page<DirectorateDTO> result = directorateService.searchDirectorate(pageable, searchTerm);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(directorateRepository, times(1)).findBySearchTerm(searchTerm.toLowerCase(), pageable);
    }

    @Test
    void givenNonMatchingSearchTerm_whenSearchDirectorate_thenReturnEmpty() {
        Pageable pageable = Pageable.unpaged();
        String searchTerm = "NonMatching";
        Page<Directorate> directoratePage = Page.empty();
        when(directorateRepository.findBySearchTerm(searchTerm.toLowerCase(), pageable)).thenReturn(directoratePage);

        Page<DirectorateDTO> result = directorateService.searchDirectorate(pageable, searchTerm);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(directorateRepository, times(1)).findBySearchTerm(searchTerm.toLowerCase(), pageable);
    }

    @Test
    void givenDirectorateId_whenGetDirectorateById_thenReturnDirectorate() {
        Long directorateId = 1L;
        Directorate directorate = new Directorate();
        directorate.setId(directorateId);
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.of(directorate));
        DirectorateDTO directorateDTO = new DirectorateDTO();
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Optional<DirectorateDTO> result = directorateService.getDirectorateById(directorateId);

        assertTrue(result.isPresent());
        verify(directorateRepository, times(1)).findById(directorateId);
    }

    @Test
    void givenNonExistentDirectorateId_whenGetDirectorateById_thenReturnEmpty() {
        Long directorateId = 1L;
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.empty());

        Optional<DirectorateDTO> result = directorateService.getDirectorateById(directorateId);

        assertFalse(result.isPresent());
        verify(directorateRepository, times(1)).findById(directorateId);
    }

    @Test
    void givenDirectorateDTO_whenCreateDirectorate_thenReturnCreatedDirectorate() {
        DirectorateDTO directorateDTO = new DirectorateDTO();
        directorateDTO.setDirectorId(1L);
        when(directorateRepository.findByDirectorId(directorateDTO.getDirectorId())).thenReturn(Optional.empty());
        Directorate directorate = new Directorate();
        when(converter.convertDTO(directorateDTO)).thenReturn(directorate);
        when(directorateRepository.save(directorate)).thenReturn(directorate);
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Optional<DirectorateDTO> result = directorateService.createDirectorate(directorateDTO);

        assertTrue(result.isPresent());
        verify(directorateRepository, times(1)).findByDirectorId(directorateDTO.getDirectorId());
        verify(directorateRepository, times(1)).save(directorate);
    }

    @Test
    void givenExistingDirectorId_whenCreateDirectorate_thenReturnEmpty() {
        DirectorateDTO directorateDTO = new DirectorateDTO();
        directorateDTO.setDirectorId(1L);
        Directorate existingDirectorate = new Directorate();
        when(directorateRepository.findByDirectorId(directorateDTO.getDirectorId())).thenReturn(Optional.of(existingDirectorate));

        Optional<DirectorateDTO> result = directorateService.createDirectorate(directorateDTO);

        assertFalse(result.isPresent());
        verify(directorateRepository, times(1)).findByDirectorId(directorateDTO.getDirectorId());
        verify(directorateRepository, times(0)).save(any(Directorate.class));
    }

    @Test
    void givenDirectorateId_whenDeleteDirectorateById_thenReturnTrue() {
        Long directorateId = 1L;
        Directorate directorate = new Directorate();
        directorate.setId(directorateId);
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.of(directorate));

        boolean result = directorateService.deleteDirectorateById(directorateId);

        assertTrue(result);
        verify(directorateRepository, times(1)).findById(directorateId);
        verify(directorateRepository, times(1)).deleteById(directorateId);
    }

    @Test
    void givenNonExistentDirectorateId_whenDeleteDirectorateById_thenReturnFalse() {
        Long directorateId = 1L;
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.empty());

        boolean result = directorateService.deleteDirectorateById(directorateId);

        assertFalse(result);
        verify(directorateRepository, times(1)).findById(directorateId);
        verify(directorateRepository, times(0)).deleteById(directorateId);
    }

    @Test
    void givenDirectorateDTO_whenUpdateDirectorate_thenReturnUpdatedDirectorate() {
        Long directorateId = 1L;
        DirectorateDTO directorateDTO = new DirectorateDTO();
        directorateDTO.setId(directorateId);
        Directorate directorate = new Directorate();
        directorate.setId(directorateId);
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.of(directorate));
        when(converter.convertDTO(directorateDTO)).thenReturn(directorate);
        when(directorateRepository.save(directorate)).thenReturn(directorate);
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Optional<DirectorateDTO> result = directorateService.updateDirectorate(directorateId, directorateDTO);

        assertTrue(result.isPresent());
        verify(directorateRepository, times(1)).findById(directorateId);
        verify(directorateRepository, times(1)).save(directorate);
    }

    @Test
    void givenNonExistentDirectorate_whenUpdateDirectorate_thenReturnEmpty() {
        Long directorateId = 1L;
        DirectorateDTO directorateDTO = new DirectorateDTO();
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.empty());

        Optional<DirectorateDTO> result = directorateService.updateDirectorate(directorateId, directorateDTO);

        assertFalse(result.isPresent());
        verify(directorateRepository, times(1)).findById(directorateId);
        verify(directorateRepository, times(0)).save(any(Directorate.class));
    }

    @Test
    void givenActiveStatus_whenUpdateDirectorateStatus_thenReturnUpdatedDirectorate() {
        Long directorateId = 1L;
        boolean active = true;
        Directorate directorate = new Directorate();
        directorate.setId(directorateId);
        directorate.setActive(!active);
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.of(directorate));
        when(directorateRepository.save(directorate)).thenReturn(directorate);
        DirectorateDTO directorateDTO = new DirectorateDTO();
        when(converter.convert(directorate)).thenReturn(directorateDTO);

        Optional<DirectorateDTO> result = directorateService.updateDirectorateStatus(directorateId, active);

        assertTrue(result.isPresent());
        verify(directorateRepository, times(1)).findById(directorateId);
        verify(directorateRepository, times(1)).save(directorate);
    }

    @Test
    void givenNonExistentDirectorate_whenUpdateDirectorateStatus_thenReturnEmpty() {
        Long directorateId = 1L;
        boolean active = true;
        when(directorateRepository.findById(directorateId)).thenReturn(Optional.empty());

        Optional<DirectorateDTO> result = directorateService.updateDirectorateStatus(directorateId, active);

        assertFalse(result.isPresent());
        verify(directorateRepository, times(1)).findById(directorateId);
        verify(directorateRepository, times(0)).save(any(Directorate.class));
    }
}
