package com.interswitch.first_api.controllers;

import com.interswitch.first_api.annotation.ValidSortField;
import com.interswitch.first_api.entities.Employee;
import com.interswitch.first_api.models.request.AddUpdateEmployeeDTO;
import com.interswitch.first_api.services.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody AddUpdateEmployeeDTO dto) {
        return employeeService.createEmployee(dto);
    }

    @GetMapping
    public Page<Employee> getAllEmployees(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @ValidSortField(target = Employee.class) @RequestParam(defaultValue = "firstName") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        return employeeService.getAllEmployees(departmentId, page, size, sortField, sortDirection);
    }
    
    @GetMapping("{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("{id}")
    public Employee updateEmployee(@PathVariable Long id, @Valid @RequestBody AddUpdateEmployeeDTO dto) {
        return employeeService.updateEmployee(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }
}