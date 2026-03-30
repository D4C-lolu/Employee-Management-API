package com.interswitch.first_api.mapper;

import com.interswitch.first_api.entities.Employee;
import com.interswitch.first_api.models.response.EmployeeDTO;

public class EmployeeMapper {
    public static EmployeeDTO toDTO(Employee employee) {
        if (employee == null) {
            return null;
        }
        return new EmployeeDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment().toString()
        );
    }
}
