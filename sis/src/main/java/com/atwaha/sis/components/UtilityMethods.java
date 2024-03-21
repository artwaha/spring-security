package com.atwaha.sis.components;

import com.atwaha.sis.model.dto.ApiResponse;
import com.atwaha.sis.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UtilityMethods {
    public ApiResponse<?> generateErrorResponse(HttpStatus status, String path, String message, Map<String, String> details) {
        return ApiResponse
                .builder()
                .path(path)
                .status(status)
                .statusCode(status.value())
                .error(ErrorResponse
                        .builder()
                        .message(message)
                        .details(details)
                        .build())
                .build();
    }

    public ApiResponse<?> generateSuccessResponse(HttpStatus status, String path, Object data) {
        return ApiResponse
                .builder()
                .path(path)
                .status(status)
                .statusCode(status.value())
                .data(data)
                .build();
    }
}
