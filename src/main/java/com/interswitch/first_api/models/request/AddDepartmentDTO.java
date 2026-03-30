package com.interswitch.first_api.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddDepartmentDTO(
    @NotBlank(message = "Department name is required")
    @Size(max = 50)
    String name
) {}


