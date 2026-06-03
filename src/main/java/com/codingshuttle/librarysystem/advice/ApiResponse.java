package com.codingshuttle.librarysystem.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse <T>{

    private T data;
    private ApiError errors;
    private LocalDateTime timestamp;

    public ApiResponse(){
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;

    }

    public ApiResponse(ApiError apiErrors){
        this();
        this.errors = apiErrors;

    }
}
