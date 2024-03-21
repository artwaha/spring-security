package com.atwaha.sis.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SchoolResponse {
    private Long id;
    private String name;
    private long students;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
