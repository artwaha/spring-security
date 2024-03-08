package com.example.Spring.Boot.tutorial.controller;

import com.example.Spring.Boot.tutorial.repository.StudentRepository;
import com.example.Spring.Boot.tutorial.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/students")
@RestController
@RequiredArgsConstructor
public class Controller {
    private final StudentRepository repository;

    @PostMapping
    Student newStudent(@RequestBody Student student) {
        return repository.save(student);
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
