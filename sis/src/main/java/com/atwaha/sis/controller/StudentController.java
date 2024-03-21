package com.atwaha.sis.controller;

import com.atwaha.sis.model.dto.StudentRequest;
import com.atwaha.sis.model.dto.StudentResponse;
import com.atwaha.sis.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/v1/students")

@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping()
    List<StudentResponse> getAllStudents() {
        return studentService.getAllStudents();
    }

    @PostMapping()
    StudentResponse addStudent(@Valid @RequestBody StudentRequest studentRequest) {
        return studentService.addStudent(studentRequest);
    }
}
