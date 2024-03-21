package com.example.Spring.Boot.tutorial.repository;

import com.example.Spring.Boot.tutorial.model.entities.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}