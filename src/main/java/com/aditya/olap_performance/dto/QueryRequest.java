package com.aditya.olap_performance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class QueryRequest {

    @Valid
    private DateRange dateRange;

    @Valid
    private Filters filters;

    @Valid
    private OrderBy orderBy;

    @Min(1)
    @Max(10000)
    @Builder.Default
    private Integer limit = 500;

    @Min(0)
    @Builder.Default
    private Integer offset = 0;
}
