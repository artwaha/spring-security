package com.atwaha.sis.service;

import com.atwaha.sis.components.DTOmapper;
import com.atwaha.sis.components.Utils;
import com.atwaha.sis.model.dto.ApiCollectionResponse;
import com.atwaha.sis.model.dto.ApiResponse;
import com.atwaha.sis.model.dto.SchoolRequest;
import com.atwaha.sis.model.dto.SchoolResponse;
import com.atwaha.sis.model.entities.School;
import com.atwaha.sis.repository.SchoolRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final DTOmapper dtOmapper;
    private final Utils util;

    public ResponseEntity<ApiResponse<SchoolResponse>> addSchool(SchoolRequest school) {
        School savedSchool = schoolRepository.save(dtOmapper.schoolRequestDTOtoSchoolEntity(school));
        SchoolResponse schoolResponse = dtOmapper.schoolEntityToSchoolResponseDTO(savedSchool);
        ApiResponse<SchoolResponse> response = util.generateGenericApiResponse(HttpStatus.CREATED.value(), schoolResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ApiCollectionResponse<SchoolResponse>> getAllSchools(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<School> schoolPage = schoolRepository.findAll(pageable);
        List<School> schoolList = schoolPage.getContent();

        List<SchoolResponse> schoolResponseList = schoolList
                .stream()
                .map(dtOmapper::schoolEntityToSchoolResponseDTO)
                .toList();

        ApiCollectionResponse<SchoolResponse> response = util.generateGenericApiCollectionResponse(HttpStatus.OK.value(), schoolPage.getNumber(), schoolPage.getSize(), schoolPage.getTotalPages(), schoolPage.getTotalElements(), schoolResponseList);
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<SchoolResponse>> updateSchool(Long schoolId, SchoolRequest schoolRequest) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new EntityNotFoundException("Invalid School Id"));
        school.setName(schoolRequest.getName());
        SchoolResponse schoolResponse = dtOmapper.schoolEntityToSchoolResponseDTO(schoolRepository.save(school));
        ApiResponse<SchoolResponse> response = util.generateGenericApiResponse(HttpStatus.OK.value(), schoolResponse);
        return ResponseEntity.ok(response);
    }
}
