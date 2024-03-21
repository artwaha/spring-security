package com.example.Spring.Boot.tutorial.controller;

import com.example.Spring.Boot.tutorial.model.entities.School;
import com.example.Spring.Boot.tutorial.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RequestMapping("api/v1/schools")
@RestController
public class SchoolController {
    private final SchoolRepository schoolRepository;

    @PostMapping
    School addSchool(@RequestBody School school) {
        return schoolRepository.save(school);
    }

    @GetMapping
    List<School> getAllSchools() {
        return schoolRepository.findAll();
    }
}
