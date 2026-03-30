package com.interswitch.first_api.models.response;

public record EmployeeDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String department
) {}
