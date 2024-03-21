package com.atwaha.sis.repository;

import com.atwaha.sis.model.entities.School;
import com.atwaha.sis.model.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    long countBySchool(School school);
}