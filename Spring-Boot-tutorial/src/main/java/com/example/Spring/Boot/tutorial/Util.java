package com.example.Spring.Boot.tutorial;

import com.example.Spring.Boot.tutorial.model.dto.StudentDTO;
import com.example.Spring.Boot.tutorial.model.entities.School;
import com.example.Spring.Boot.tutorial.model.entities.Student;

public class Util {
    public static Student studentDTOtoStudent(StudentDTO studentDTO) {
        return Student.builder()
                .firstName(studentDTO.firstName())
                .lastName(studentDTO.lastName())
                .email(studentDTO.email())
                .age(studentDTO.age())
                .school(School.builder().id(studentDTO.schoolId()).build())
                .build();
    }
}
