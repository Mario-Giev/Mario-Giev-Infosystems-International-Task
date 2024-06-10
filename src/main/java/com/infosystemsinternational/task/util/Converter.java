package com.infosystemsinternational.task.util;

import com.infosystemsinternational.task.dto.CreateEmployeeDTO;
import com.infosystemsinternational.task.dto.DepartmentDTO;
import com.infosystemsinternational.task.dto.EmployeeDTO;
import com.infosystemsinternational.task.dto.DirectorateDTO;
import com.infosystemsinternational.task.entity.Department;
import com.infosystemsinternational.task.entity.Directorate;
import com.infosystemsinternational.task.entity.Employee;
import com.infosystemsinternational.task.enums.Position;
import com.infosystemsinternational.task.repository.DepartmentRepository;
import com.infosystemsinternational.task.repository.DirectorateRepository;
import com.infosystemsinternational.task.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Utility class for converting between entity objects and DTOs.
 */
@Component
@AllArgsConstructor
public class Converter {

    private final DepartmentRepository departmentRepository;
    private final DirectorateRepository directorateRepository;
    private final EmployeeRepository employeeRepository;

    /**
     * Converts an Employee entity to an EmployeeDTO.
     *
     * @param employee the Employee entity to convert
     * @return the converted EmployeeDTO
     */
    public EmployeeDTO convert(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setActive(employee.isActive());
        dto.setName(employee.getName());
        dto.setSurname(employee.getSurname());
        dto.setPersonalId(employee.getPersonalId());
        dto.setAge(employee.getAge());
        dto.setPosition(employee.getPosition().getLabel());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(employee.getDepartment().getId());
        }

        return dto;
    }

    /**
     * Converts an EmployeeDTO to an Employee entity.
     *
     * @param dto the EmployeeDTO to convert
     * @return the converted Employee entity
     */
    public Employee convertDTO(EmployeeDTO dto) {
        Employee employee = new Employee();

        employee.setId(dto.getId());
        employee.setActive(dto.isActive());
        employee.setName(dto.getName());
        employee.setSurname(dto.getSurname());
        employee.setPersonalId(dto.getPersonalId());
        employee.setAge(dto.getAge());
        employee.setPosition(Position.fromLabel(dto.getPosition()));

        if (dto.getId() != null) {
            Optional<Employee> employeeOptional = employeeRepository.findById(dto.getId());
            if (employeeOptional.isPresent()) {
                employee.setPassword(employeeOptional.get().getPassword());
            }
        }

        if (dto.getDepartmentId() != null) {
            Optional<Department> departmentOptional = departmentRepository.findById(dto.getDepartmentId());
            if (departmentOptional.isPresent()) {
                employee.setDepartment(departmentOptional.get());
            }
        }

        return employee;
    }

    /**
     * Converts an CreateEmployeeDTO to an Employee entity.
     *
     * @param dto the EmployeeDTO to convert
     * @return the converted Employee entity
     */
    public Employee convertDTO(CreateEmployeeDTO dto) {
        Employee employee = new Employee();

        employee.setActive(dto.isActive());
        employee.setName(dto.getName());
        employee.setSurname(dto.getSurname());
        employee.setPersonalId(dto.getPersonalId());
        employee.setPassword(dto.getPassword());
        employee.setAge(dto.getAge());
        employee.setPosition(Position.fromLabel(dto.getPosition()));

        if (dto.getDepartmentId() != null) {
            Optional<Department> departmentOptional = departmentRepository.findById(dto.getDepartmentId());
            if (departmentOptional.isPresent()) {
                employee.setDepartment(departmentOptional.get());
            }
        }

        return employee;
    }

    /**
     * Converts a Department entity to a DepartmentDTO.
     *
     * @param department the Department entity to convert
     * @return the converted DepartmentDTO
     */
    public DepartmentDTO convert(Department department) {
        DepartmentDTO dto = new DepartmentDTO();

        dto.setId(department.getId());
        dto.setActive(department.isActive());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());

        if (department.getDirectorate() != null) {
            dto.setDirectorateId(department.getDirectorate().getId());
        }
        return dto;
    }

    /**
     * Converts a DepartmentDTO to a Department entity.
     *
     * @param dto the DepartmentDTO to convert
     * @return the converted Department entity
     */
    public Department convertDTO(DepartmentDTO dto) {
        Department department = new Department();

        department.setId(dto.getId());
        department.setActive(dto.isActive());
        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        Optional<Directorate> directorateOptional = directorateRepository.findById(dto.getDirectorateId());
        if (directorateOptional.isPresent()) {
            department.setDirectorate(directorateOptional.get());
        }

        return department;
    }

    /**
     * Converts a Directorate entity to a DirectorateDTO.
     *
     * @param directorate the Directorate entity to convert
     * @return the converted DirectorateDTO
     */
    public DirectorateDTO convert(Directorate directorate) {
        DirectorateDTO dto = new DirectorateDTO();

        dto.setId(directorate.getId());
        dto.setActive(directorate.isActive());
        dto.setName(directorate.getName());
        dto.setDescription(directorate.getDescription());

        if (directorate.getDirector() != null) {
            dto.setDirectorId(directorate.getDirector().getId());
        }
        return dto;
    }

    /**
     * Converts a DirectorateDTO to a Directorate entity.
     *
     * @param dto the DirectorateDTO to convert
     * @return the converted Directorate entity
     */
    public Directorate convertDTO(DirectorateDTO dto) {
        Directorate directorate = new Directorate();

        directorate.setId(dto.getId());
        directorate.setActive(dto.isActive());
        directorate.setName(dto.getName());
        directorate.setDescription(dto.getDescription());

        Optional<Employee> employeeOptional = employeeRepository.findById(dto.getDirectorId());
        if (employeeOptional.isPresent()) {
            directorate.setDirector(employeeOptional.get());
        }

        return directorate;
    }
}
