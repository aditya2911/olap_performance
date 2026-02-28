package com.aditya.olap_performance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filters {

    @Valid
    @NotNull(message = "and filters are required")
    @Size(min = 1, message = "at least one filter is required")
    private List<FilterCondition> and;
}
