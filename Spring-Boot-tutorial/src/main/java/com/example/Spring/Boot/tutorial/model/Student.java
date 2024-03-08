package com.example.Spring.Boot.tutorial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue()
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;

    @OneToOne(cascade = CascadeType.ALL)
    private StudentProfile studentProfile;

    @ManyToOne
    private School school;
}
