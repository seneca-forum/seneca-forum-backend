package com.seneca.senecaforum.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiExceptionResponse {
    private String message;
    private int httpStatus;
    private long timestamp;
}
