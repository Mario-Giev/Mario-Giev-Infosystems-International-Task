package com.infosystemsinternational.task.service;

import com.infosystemsinternational.task.dto.DepartmentDTO;
import com.infosystemsinternational.task.entity.Department;
import com.infosystemsinternational.task.repository.DepartmentRepository;
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

class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private Converter converter;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenActiveDepartmentsOnly_whenGetAllDepartments_thenReturnActiveDepartments() {
        Pageable pageable = Pageable.unpaged();
        Department department1 = new Department();
        department1.setActive(true);
        Page<Department> departmentPage = new PageImpl<>(Arrays.asList(department1));
        when(departmentRepository.findByActive(true, pageable)).thenReturn(departmentPage);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(converter.convert(department1)).thenReturn(departmentDTO);

        Page<DepartmentDTO> result = departmentService.getAllDepartments(pageable, true);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(departmentRepository, times(1)).findByActive(true, pageable);
    }

    @Test
    void givenAllDepartments_whenGetAllDepartments_thenReturnAllDepartments() {
        Pageable pageable = Pageable.unpaged();
        Department department = new Department();
        Page<Department> departmentPage = new PageImpl<>(Arrays.asList(department));
        when(departmentRepository.findAll(pageable)).thenReturn(departmentPage);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(converter.convert(department)).thenReturn(departmentDTO);

        Page<DepartmentDTO> result = departmentService.getAllDepartments(pageable, false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(departmentRepository, times(1)).findAll(pageable);
    }

    @Test
    void givenSearchTerm_whenSearchDepartments_thenReturnMatchingDepartments() {
        Pageable pageable = Pageable.unpaged();
        String searchTerm = "Finance";
        Department department = new Department();
        Page<Department> departmentPage = new PageImpl<>(Arrays.asList(department));
        when(departmentRepository.findBySearchTerm(searchTerm.toLowerCase(), pageable)).thenReturn(departmentPage);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(converter.convert(department)).thenReturn(departmentDTO);

        Page<DepartmentDTO> result = departmentService.searchDepartments(pageable, searchTerm);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(departmentRepository, times(1)).findBySearchTerm(searchTerm.toLowerCase(), pageable);
    }

    @Test
    void givenNonMatchingSearchTerm_whenSearchDepartments_thenReturnEmpty() {
        Pageable pageable = Pageable.unpaged();
        String searchTerm = "NonMatching";
        Page<Department> departmentPage = Page.empty();
        when(departmentRepository.findBySearchTerm(searchTerm.toLowerCase(), pageable)).thenReturn(departmentPage);

        Page<DepartmentDTO> result = departmentService.searchDepartments(pageable, searchTerm);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(departmentRepository, times(1)).findBySearchTerm(searchTerm.toLowerCase(), pageable);
    }

    @Test
    void givenDepartmentId_whenGetDepartmentById_thenReturnDepartment() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(converter.convert(department)).thenReturn(departmentDTO);

        Optional<DepartmentDTO> result = departmentService.getDepartmentById(departmentId);

        assertTrue(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void givenNonExistentDepartmentId_whenGetDepartmentById_thenReturnEmpty() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        Optional<DepartmentDTO> result = departmentService.getDepartmentById(departmentId);

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
    }

    @Test
    void givenDepartmentDTO_whenCreateDepartment_thenReturnCreatedDepartment() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        Department department = new Department();
        when(converter.convertDTO(departmentDTO)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(converter.convert(department)).thenReturn(departmentDTO);

        Optional<DepartmentDTO> result = departmentService.createDepartment(departmentDTO);

        assertTrue(result.isPresent());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void givenDepartmentId_whenDeleteDepartmentById_thenReturnTrue() {
        Long departmentId = 1L;
        Department department = new Department();
        department.setId(departmentId);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));

        boolean result = departmentService.deleteDepartmentById(departmentId);

        assertTrue(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).deleteById(departmentId);
    }

    @Test
    void givenNonExistentDepartmentId_whenDeleteDepartmentById_thenReturnFalse() {
        Long departmentId = 1L;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        boolean result = departmentService.deleteDepartmentById(departmentId);

        assertFalse(result);
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(0)).deleteById(departmentId);
    }

    @Test
    void givenDepartmentDTO_whenUpdateDepartment_thenReturnUpdatedDepartment() {
        Long departmentId = 1L;
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(departmentId);
        Department department = new Department();
        department.setId(departmentId);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(converter.convertDTO(departmentDTO)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(converter.convert(department)).thenReturn(departmentDTO);

        Optional<DepartmentDTO> result = departmentService.updateDepartment(departmentId, departmentDTO);

        assertTrue(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void givenNonExistentDepartment_whenUpdateDepartment_thenReturnEmpty() {
        Long departmentId = 1L;
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        Optional<DepartmentDTO> result = departmentService.updateDepartment(departmentId, departmentDTO);

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(0)).save(any(Department.class));
    }

    @Test
    void givenActiveStatus_whenUpdateDepartmentStatus_thenReturnUpdatedDepartment() {
        Long departmentId = 1L;
        boolean active = true;
        Department department = new Department();
        department.setId(departmentId);
        department.setActive(!active);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(department);
        DepartmentDTO departmentDTO = new DepartmentDTO();
        when(converter.convert(department)).thenReturn(departmentDTO);

        Optional<DepartmentDTO> result = departmentService.updateDepartmentStatus(departmentId, active);

        assertTrue(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void givenNonExistentDepartment_whenUpdateDepartmentStatus_thenReturnEmpty() {
        Long departmentId = 1L;
        boolean active = true;
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        Optional<DepartmentDTO> result = departmentService.updateDepartmentStatus(departmentId, active);

        assertFalse(result.isPresent());
        verify(departmentRepository, times(1)).findById(departmentId);
        verify(departmentRepository, times(0)).save(any(Department.class));
    }
}
