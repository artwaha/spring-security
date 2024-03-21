package com.example.Spring.Boot.tutorial.repository;

import com.example.Spring.Boot.tutorial.model.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}