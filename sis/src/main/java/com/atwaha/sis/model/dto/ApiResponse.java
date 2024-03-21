package com.atwaha.sis.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String path;
    private HttpStatus status;
    @JsonProperty("status-code")
    private int statusCode;

    private T data;
    private ErrorResponse error;
}
