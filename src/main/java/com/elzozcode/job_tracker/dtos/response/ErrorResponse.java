package com.elzozcode.job_tracker.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String message;
}
