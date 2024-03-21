package com.atwaha.sis.service;

import com.atwaha.sis.components.DTOmapper;
import com.atwaha.sis.components.UtilityMethods;
import com.atwaha.sis.model.dto.ApiResponse;
import com.atwaha.sis.model.dto.SchoolRequest;
import com.atwaha.sis.model.dto.SchoolResponse;
import com.atwaha.sis.model.entities.School;
import com.atwaha.sis.repository.SchoolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;
    private final DTOmapper dtOmapper;
    private final UtilityMethods util;

    public ResponseEntity<ApiResponse<SchoolResponse>> addSchool(SchoolRequest school) {
        School savedSchool = schoolRepository.save(dtOmapper.schoolRequestDTOtoSchoolEntity(school));

        return null;
    }

    public List<SchoolResponse> getAllSchools() {
        List<School> schoolList = schoolRepository.findAll();
        return schoolList
                .stream()
                .map(dtOmapper::schoolEntityToSchoolResponseDTO)
                .toList();
    }


    public SchoolResponse updateSchool(Long schoolId, SchoolRequest schoolRequest) {
        School school = schoolRepository.findById(schoolId).orElseThrow(() -> new EntityNotFoundException("Invalid School Id"));

        school.setName(schoolRequest.getName());

        return dtOmapper.schoolEntityToSchoolResponseDTO(schoolRepository.save(school));
    }
}
