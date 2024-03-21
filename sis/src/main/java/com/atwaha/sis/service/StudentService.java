package com.atwaha.sis.service;

import com.atwaha.sis.components.DTOmapper;
import com.atwaha.sis.model.dto.StudentRequest;
import com.atwaha.sis.model.dto.StudentResponse;
import com.atwaha.sis.model.entities.Student;
import com.atwaha.sis.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final DTOmapper dtOmapper;


    public List<StudentResponse> getAllStudents() {
        List<Student> studentList = studentRepository.findAll();

        return studentList.stream().map(dtOmapper::studentEntityToStudentResponseDTO).toList();
    }

    public StudentResponse addStudent(StudentRequest studentRequest) {
        Student savedStudent = studentRepository.save(dtOmapper.studentRequestDTOtoStudentEntity(studentRequest));

        return dtOmapper.studentEntityToStudentResponseDTO(savedStudent);
    }
}
