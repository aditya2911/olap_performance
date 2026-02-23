package com.aditya.olap_performance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {

    private List<T> data;
    private Long totalCount;
    private Boolean hasMore;
    private Integer limit;
    private Integer offset;
}
