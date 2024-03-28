package com.atwaha.sis.controller;

import com.atwaha.sis.model.dto.ApiCollectionResponse;
import com.atwaha.sis.model.dto.ApiResponse;
import com.atwaha.sis.model.dto.StudentRequest;
import com.atwaha.sis.model.dto.StudentResponse;
import com.atwaha.sis.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("api/v1/students")
@RequiredArgsConstructor
@Tag(name = "Student")
@SecurityRequirement(name = "JWT")
public class StudentController {
    private final StudentService studentService;

    @PostMapping()
    @Operation(summary = "Create new Student")
    ResponseEntity<ApiResponse<StudentResponse>> addStudent(@Valid @RequestBody StudentRequest studentRequest) {
        return studentService.addStudent(studentRequest);
    }

    @GetMapping()
    ResponseEntity<ApiCollectionResponse<StudentResponse>> getAllStudents(@Valid @RequestParam(defaultValue = "0", required = false) int pageNumber, @Valid @RequestParam(defaultValue = "10", required = false) int pageSize) {
        return studentService.getAllStudents(pageNumber, pageSize);
    }
}
