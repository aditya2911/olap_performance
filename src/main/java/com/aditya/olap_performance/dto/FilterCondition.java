package com.aditya.olap_performance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCondition {

    @NotBlank(message = "field is required")
    private String field;

    @NotBlank(message = "operator is required")
    private String op;

    @NotNull(message = "value is required")
    private Object value;
}
