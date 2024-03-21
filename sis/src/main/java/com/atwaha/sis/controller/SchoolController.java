package com.atwaha.sis.controller;

import com.atwaha.sis.model.dto.ApiResponse;
import com.atwaha.sis.model.dto.SchoolRequest;
import com.atwaha.sis.model.dto.SchoolResponse;
import com.atwaha.sis.service.SchoolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/schools")
@RequiredArgsConstructor
//@SecurityRequirement(name = "JWT - Bearer Authentication")

@Tag(name = "School")
public class SchoolController {
    private final SchoolService schoolService;

    @PostMapping
    @Operation(summary = "Create new School")
    ResponseEntity<ApiResponse<SchoolResponse>> addSchool(@Valid @RequestBody SchoolRequest school) {
        return schoolService.addSchool(school);
    }

    @GetMapping
    List<SchoolResponse> getAllSchools() {
        return schoolService.getAllSchools();
    }

    @PatchMapping("{school-id}")
//    @Operation(
//            summary = "Update School Details",
//            parameters = {
//                    @Parameter(name = "school-id", required = true, example = "123")
//            }
//    )
    SchoolResponse updateSchool(@Valid @PathVariable(name = "school-id") Long schoolId, @Valid @RequestBody SchoolRequest schoolRequest) {
        return schoolService.updateSchool(schoolId, schoolRequest);
    }
}
