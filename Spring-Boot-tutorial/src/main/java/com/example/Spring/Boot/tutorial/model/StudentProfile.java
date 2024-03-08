package com.example.Spring.Boot.tutorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "student_profiles")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentProfile {
    @Id
    @GeneratedValue
    private Long id;
    private String bio;

    @OneToOne
    private Student student;
}
