package com.atwaha.sis.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SchoolRequest {
    @NotNull(message = "School Name is Required")
    private String name;
}
