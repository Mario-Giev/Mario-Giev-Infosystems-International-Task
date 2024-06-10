package com.infosystemsinternational.task.service;

import com.infosystemsinternational.task.dto.CreateEmployeeDTO;
import com.infosystemsinternational.task.dto.EmployeeDTO;
import com.infosystemsinternational.task.entity.Employee;
import com.infosystemsinternational.task.enums.Position;
import com.infosystemsinternational.task.repository.EmployeeRepository;
import com.infosystemsinternational.task.util.Converter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Converter converter;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        converter = new Converter(null, null, employeeRepository);
        ReflectionTestUtils.setField(employeeService, "converter", converter);
    }

    @Test
    void givenActiveEmployeesOnly_whenGetAllEmployees_thenReturnActiveEmployees() {
        Pageable pageable = Pageable.unpaged();
        Employee employee = new Employee();
        employee.setActive(true);
        employee.setPosition(Position.EMPLOYEE);
        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(employee));
        when(employeeRepository.findByActive(true, pageable)).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.getAllEmployees(pageable, true);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findByActive(true, pageable);
    }

    @Test
    void givenInactiveEmployeesOnly_whenGetAllEmployees_thenReturnInactiveEmployees() {
        Pageable pageable = Pageable.unpaged();
        Employee employee = new Employee();
        employee.setActive(false);
        employee.setPosition(Position.EMPLOYEE);
        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(employee));
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.getAllEmployees(pageable, false);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(pageable);
    }

    @Test
    void givenSearchTerm_whenSearchEmployees_thenReturnMatchingEmployees() {
        Pageable pageable = Pageable.unpaged();
        String searchTerm = "John";
        Employee employee = new Employee();
        employee.setName("John");
        employee.setPosition(Position.EMPLOYEE);
        Page<Employee> employeePage = new PageImpl<>(Arrays.asList(employee));
        when(employeeRepository.findByNameOrSurname(searchTerm.toLowerCase(), pageable)).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.searchEmployees(pageable, searchTerm);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(employeeRepository, times(1)).findByNameOrSurname(searchTerm.toLowerCase(), pageable);
    }

    @Test
    void givenNonMatchingSearchTerm_whenSearchEmployees_thenReturnEmpty() {
        Pageable pageable = Pageable.unpaged();
        String searchTerm = "NonMatching";
        Page<Employee> employeePage = Page.empty();
        when(employeeRepository.findByNameOrSurname(searchTerm.toLowerCase(), pageable)).thenReturn(employeePage);

        Page<EmployeeDTO> result = employeeService.searchEmployees(pageable, searchTerm);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(employeeRepository, times(1)).findByNameOrSurname(searchTerm.toLowerCase(), pageable);
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPosition(Position.EMPLOYEE);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        Optional<EmployeeDTO> result = employeeService.getEmployeeById(employeeId);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void givenNonExistentEmployeeId_whenGetEmployeeById_thenReturnEmpty() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = employeeService.getEmployeeById(employeeId);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
    }

    @Test
    void givenCreateEmployeeDTO_whenCreateEmployee_thenReturnCreatedEmployee() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setPersonalId("12345");
        createEmployeeDTO.setPosition("Employee");
        Employee employee = new Employee();
        employee.setPersonalId("12345");
        employee.setPosition(Position.EMPLOYEE);
        when(employeeRepository.findByPersonalId("12345")).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Optional<EmployeeDTO> result = employeeService.createEmployee(createEmployeeDTO);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findByPersonalId("12345");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void givenExistingPersonalId_whenCreateEmployee_thenReturnEmpty() {
        CreateEmployeeDTO createEmployeeDTO = new CreateEmployeeDTO();
        createEmployeeDTO.setPersonalId("12345");
        Employee existingEmployee = new Employee();
        existingEmployee.setPersonalId("12345");
        when(employeeRepository.findByPersonalId("12345")).thenReturn(Optional.of(existingEmployee));

        Optional<EmployeeDTO> result = employeeService.createEmployee(createEmployeeDTO);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findByPersonalId("12345");
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void givenEmployeeId_whenDeleteEmployeeById_thenReturnTrue() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        boolean result = employeeService.deleteEmployeeById(employeeId);

        assertTrue(result);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

    @Test
    void givenNonExistentEmployeeId_whenDeleteEmployeeById_thenReturnFalse() {
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        boolean result = employeeService.deleteEmployeeById(employeeId);

        assertFalse(result);
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(0)).deleteById(employeeId);
    }

    @Test
    void givenEmployeeDTO_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employeeId);
        employeeDTO.setPosition("Employee");
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPosition(Position.EMPLOYEE);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Optional<EmployeeDTO> result = employeeService.updateEmployee(employeeId, employeeDTO);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(2)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void givenNonExistentEmployee_whenUpdateEmployee_thenReturnEmpty() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = employeeService.updateEmployee(employeeId, employeeDTO);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void givenNewPassword_whenUpdateEmployeePassword_thenReturnUpdatedEmployee() {
        Long employeeId = 1L;
        String newPassword = "newPassword";
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setPosition(Position.EMPLOYEE);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        Optional<EmployeeDTO> result = employeeService.updateEmployeePassword(employeeId, newPassword);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    void givenNonExistentEmployee_whenUpdateEmployeePassword_thenReturnEmpty() {
        Long employeeId = 1L;
        String newPassword = "newPassword";
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = employeeService.updateEmployeePassword(employeeId, newPassword);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(0)).save(any(Employee.class));
        verify(passwordEncoder, times(0)).encode(newPassword);
    }

    @Test
    void givenActiveStatus_whenUpdateEmployeeStatus_thenReturnUpdatedEmployee() {
        Long employeeId = 1L;
        boolean active = true;
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setActive(!active);
        employee.setPosition(Position.EMPLOYEE);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Optional<EmployeeDTO> result = employeeService.updateEmployeeStatus(employeeId, active);

        assertTrue(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void givenNonExistentEmployee_whenUpdateEmployeeStatus_thenReturnEmpty() {
        Long employeeId = 1L;
        boolean active = true;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        Optional<EmployeeDTO> result = employeeService.updateEmployeeStatus(employeeId, active);

        assertFalse(result.isPresent());
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }
}
