package com.atwaha.sis.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class StudentResponse {
    private Long id;
    private String fullName;
    private String email;
    private SchoolResponse school;

    private Instant createdDate;
    private Long createdBy;
    private Instant lastModifiedDate;
    private Long lastModifiedBy;
}
