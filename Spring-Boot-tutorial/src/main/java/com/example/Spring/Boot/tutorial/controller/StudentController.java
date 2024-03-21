package com.example.Spring.Boot.tutorial.controller;

import com.example.Spring.Boot.tutorial.Util;
import com.example.Spring.Boot.tutorial.model.dto.StudentDTO;
import com.example.Spring.Boot.tutorial.model.entities.Student;
import com.example.Spring.Boot.tutorial.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/students")
@RestController
@RequiredArgsConstructor
public class StudentController {
    private final StudentRepository repository;


    @PostMapping
    Student newStudent(@RequestBody StudentDTO studentDTO) {
        Student newStudent = Util.studentDTOtoStudent(studentDTO);
        return repository.save(newStudent);
    }

    @GetMapping
    List<Student> getAllStudent() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    Student getStudentById(@PathVariable Long id) {
        return repository.findById(id).orElse(Student.builder().build());
    }

    @GetMapping("search/{name}")
    List<Student> getAllStudent(@PathVariable String name) {
        return repository.findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }
}
