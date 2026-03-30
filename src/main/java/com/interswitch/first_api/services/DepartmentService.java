package com.interswitch.first_api.services;

import com.interswitch.first_api.entities.Department;
import com.interswitch.first_api.exceptions.ConflictException;
import com.interswitch.first_api.exceptions.NotFoundException;
import com.interswitch.first_api.models.request.AddDepartmentDTO;
import com.interswitch.first_api.models.response.DepartmentDTO;
import com.interswitch.first_api.repositories.DepartmentRepository;
import com.interswitch.first_api.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentDTO createDepartment(AddDepartmentDTO dto) {
        log.info("Creating department: {}", dto.name());

        if (departmentRepository.existsByName(dto.name()))
            throw new ConflictException("Department already exists with name: " + dto.name());

        Department department = new Department();
        department.setName(dto.name());

        Department saved = departmentRepository.save(department);
        return new DepartmentDTO(saved.getId(), saved.getName());
    }

    public List<DepartmentDTO> getAllDepartments() {
        log.info("Fetching all departments");
        return departmentRepository.findAll()
                .stream()
                .map(d -> new DepartmentDTO(d.getId(), d.getName()))
                .toList();
    }

    public DepartmentDTO getDepartmentById(Long id) {
        log.info("Fetching department with id: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found with id: " + id));
        return new DepartmentDTO(department.getId(), department.getName());
    }

    public DepartmentDTO updateDepartment(Long id, AddDepartmentDTO dto) {
        log.info("Updating department with id: {}", id);

        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found with id: " + id));

        if (departmentRepository.existsByName(dto.name()))
            throw new ConflictException("Department already exists with name: " + dto.name());

        existing.setName(dto.name());
        Department saved = departmentRepository.save(existing);
        return new DepartmentDTO(saved.getId(), saved.getName());
    }

    public void deleteDepartment(Long id) {
        log.info("Deleting department with id: {}", id);

        if (!departmentRepository.existsById(id))
            throw new NotFoundException("Department not found with id: " + id);

        if (employeeRepository.existsByDepartment_Id(id))
            throw new ConflictException("Cannot delete department with existing employees");

        departmentRepository.deleteById(id);
    }
}