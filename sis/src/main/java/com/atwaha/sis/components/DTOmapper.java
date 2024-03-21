package com.atwaha.sis.components;

import com.atwaha.sis.model.dto.SchoolRequest;
import com.atwaha.sis.model.dto.SchoolResponse;
import com.atwaha.sis.model.dto.StudentRequest;
import com.atwaha.sis.model.dto.StudentResponse;
import com.atwaha.sis.model.entities.School;
import com.atwaha.sis.model.entities.Student;
import com.atwaha.sis.repository.StudentRepository;
import com.atwaha.sis.repository.SchoolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DTOmapper {
    private final SchoolRepository schoolRepository;
    private final ModelMapper modelMapper;
    private final StudentRepository studentRepository;

    public Student studentRequestDTOtoStudentEntity(StudentRequest studentRequest) {
        School school = schoolRepository.findById(studentRequest.getSchoolId()).orElseThrow(() -> new EntityNotFoundException("Invalid School Id"));

        Student newStudent = modelMapper.map(studentRequest, Student.class);
        newStudent.setSchool(school);

        return newStudent;
    }

    public StudentResponse studentEntityToStudentResponseDTO(Student student) {
        SchoolResponse schoolResponse = modelMapper.map(student.getSchool(), SchoolResponse.class);
        long studentsCount = studentRepository.countBySchool(student.getSchool());
        schoolResponse.setStudents(studentsCount);

        StudentResponse studentResponse = modelMapper.map(student, StudentResponse.class);
        studentResponse.setSchool(schoolResponse);
        studentResponse.setFullName(student.getFirstName().concat(" ").concat(student.getLastName()));

        return studentResponse;
    }

    public School schoolRequestDTOtoSchoolEntity(SchoolRequest schoolRequest) {
        return modelMapper.map(schoolRequest, School.class);
    }

    public SchoolResponse schoolEntityToSchoolResponseDTO(School school) {
        long studentsCount = studentRepository.countBySchool(school);

        SchoolResponse schoolResponse = modelMapper.map(school, SchoolResponse.class);
        schoolResponse.setStudents(studentsCount);

        return schoolResponse;
    }
}
