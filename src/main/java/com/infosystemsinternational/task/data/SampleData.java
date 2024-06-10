package com.infosystemsinternational.task.data;

import com.infosystemsinternational.task.entity.Department;
import com.infosystemsinternational.task.entity.Directorate;
import com.infosystemsinternational.task.entity.Employee;
import com.infosystemsinternational.task.enums.Position;
import com.infosystemsinternational.task.repository.DepartmentRepository;
import com.infosystemsinternational.task.repository.DirectorateRepository;
import com.infosystemsinternational.task.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Component that loads sample data into the database on application startup.
 */
@Component
@RequiredArgsConstructor
public class SampleData implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DirectorateRepository directorateRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Method that will be run after the application context is loaded and right before the Spring Application run method is completed.
     * This method checks if all repositories are empty and loads sample data if they are.
     *
     * @param args incoming main method arguments
     * @throws Exception if an error occurs while loading sample data
     */
    @Override
    public void run(String... args) throws Exception {
        if (directorateRepository.findAll().isEmpty() && departmentRepository.findAll().isEmpty() && employeeRepository.findAll().isEmpty()) {
            Directorate dir1 = new Directorate("Finance", "Finance Directorate");
            Directorate dir2 = new Directorate("IT", "IT Directorate");
            Directorate dir3 = new Directorate("HR", "HR Directorate");
            Directorate dir4 = new Directorate("Operations", "Operations Directorate");
            Directorate dir5 = new Directorate("Marketing", "Marketing Directorate");

            directorateRepository.saveAll(Arrays.asList(dir1, dir2, dir3, dir4, dir5));

            Department dep1 = new Department("Accounts", "Accounts Department", dir1);
            Department dep2 = new Department("Infrastructure", "Infrastructure Department", dir2);
            Department dep3 = new Department("Recruitment", "Recruitment Department", dir3);
            Department dep4 = new Department("Logistics", "Logistics Department", dir4);
            Department dep5 = new Department("Sales", "Sales Department", dir5);

            departmentRepository.saveAll(Arrays.asList(dep1, dep2, dep3, dep4, dep5));

            Employee emp1 = new Employee("Peter", "Petrov", "10001", passwordEncoder.encode("password"), 30, Position.DIRECTORATE_DIRECTOR, dep1);
            Employee emp2 = new Employee("Ivan", "Ivanov", "10002", passwordEncoder.encode("password"), 25, Position.DEPARTMENT_HEAD, dep1);
            Employee emp3 = new Employee("Georgi", "Georgiev", "10003", passwordEncoder.encode("password"), 40, Position.DEPARTMENT_HEAD, dep1);
            Employee emp4 = new Employee("Aleksandar", "Aleksandrov", "10004", passwordEncoder.encode("password"), 35, Position.EMPLOYEE, dep1);
                Employee emp5 = new Employee("Mario", "Giev", "10005", passwordEncoder.encode("password"), 28, Position.EMPLOYEE, dep1);

            employeeRepository.saveAll(Arrays.asList(emp1, emp2, emp3, emp4, emp5));

            dir1.setDirector(emp1);
            directorateRepository.save(dir1);
        }
    }
}
