package com.interswitch.first_api.services;

import com.interswitch.first_api.entities.Department;
import com.interswitch.first_api.entities.Employee;
import com.interswitch.first_api.exceptions.BadRequestException;
import com.interswitch.first_api.exceptions.ConflictException;
import com.interswitch.first_api.exceptions.NotFoundException;
import com.interswitch.first_api.repositories.DepartmentRepository;
import com.interswitch.first_api.repositories.EmployeeRepository;
import com.interswitch.first_api.models.request.AddUpdateEmployeeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public Employee createEmployee(AddUpdateEmployeeDTO dto) {
        log.info("Creating employee: {}", dto.firstName());

        if (employeeRepository.existsByEmail(dto.email()))
            throw new ConflictException("Email already exists");

        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new NotFoundException("Department not found with id: " + dto.departmentId()));

        Employee employee = Employee.builder()
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .department(department)
                .build();

        return employeeRepository.save(employee);
    }


    public Employee getEmployeeById(Long id) {
        log.info("Fetching employee with id: {}", id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with id: " + id));
    }

    public Employee updateEmployee(Long id, AddUpdateEmployeeDTO dto) {
        log.info("Updating employee with id: {}", id);

        if (employeeRepository.existsByEmailAndIdNot(dto.email(), id))
            throw new ConflictException("Email already exists");

        Employee existing = getEmployeeById(id);

        Department department = departmentRepository.findById(dto.departmentId())
                .orElseThrow(() -> new NotFoundException("Department not found with id: " + dto.departmentId()));

        Employee updated = Employee.builder()
                .id(existing.getId())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .department(department)
                .build();

        return employeeRepository.save(updated);
    }

    public Page<Employee> getAllEmployees(Long departmentId, int page, int size,
                                          String sortField, Sort.Direction sortDirection) {
        log.info("Fetching employees - departmentId: {}, page: {}, size: {}", departmentId, page, size);
        page = page - 1;

        Sort sort = sortDirection == Sort.Direction.ASC
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        if (departmentId != null) {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new NotFoundException("Department not found with id: " + departmentId));
            return employeeRepository.findByDepartment(department, pageable);
        }

        return employeeRepository.findAll(pageable);
    }

    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);
        employeeRepository.findById(id)
                .ifPresentOrElse(
                        employeeRepository::delete,
                        () -> { throw new BadRequestException("Employee not found with id: " + id); }
                );
    }
}
