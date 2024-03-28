package com.atwaha.sis.model.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class SchoolResponse {
    private Long id;
    private String name;
    private long students;

    private Instant createdDate;
    private Long createdBy;
    private Instant lastModifiedDate;
    private Long lastModifiedBy;
}
