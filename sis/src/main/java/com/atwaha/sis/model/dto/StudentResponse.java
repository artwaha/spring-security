package com.atwaha.sis.model.dto;

import lombok.Data;

@Data
public class StudentResponse {
    private Long id;

    private String fullName;

    private String email;

    private SchoolResponse school;
}
