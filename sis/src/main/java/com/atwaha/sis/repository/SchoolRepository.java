package com.atwaha.sis.repository;

import com.atwaha.sis.model.entities.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}