package com.atwaha.sis.service;

import com.atwaha.sis.components.DTOmapper;
import com.atwaha.sis.components.Utils;
import com.atwaha.sis.model.dto.ApiCollectionResponse;
import com.atwaha.sis.model.dto.ApiResponse;
import com.atwaha.sis.model.dto.StudentRequest;
import com.atwaha.sis.model.dto.StudentResponse;
import com.atwaha.sis.model.entities.Student;
import com.atwaha.sis.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final DTOmapper dtOmapper;
    private final Utils util;

    public ResponseEntity<ApiCollectionResponse<StudentResponse>> getAllStudents(int pageNumber, int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);
        Page<Student> studentPage = studentRepository.findAll(pageableRequest);

        List<Student> studentList = studentPage.getContent();
        List<StudentResponse> studentResponseList = studentList.stream().map(dtOmapper::studentEntityToStudentResponseDTO).toList();

        ApiCollectionResponse<StudentResponse> response = util.generateGenericApiCollectionResponse(HttpStatus.OK.value(), studentPage.getNumber(), studentPage.getSize(), studentPage.getTotalPages(), studentPage.getTotalElements(), studentResponseList);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<StudentResponse>> addStudent(StudentRequest studentRequest) {
        Student savedStudent = studentRepository.save(dtOmapper.studentRequestDTOtoStudentEntity(studentRequest));
        StudentResponse studentResponse = dtOmapper.studentEntityToStudentResponseDTO(savedStudent);
        ApiResponse<StudentResponse> response = util.generateGenericApiResponse(HttpStatus.CREATED.value(), studentResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
