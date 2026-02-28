package com.aditya.olap_performance.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DateRange {

    @NotNull(message = "start date is required")
    private LocalDateTime start;

    @NotNull(message = "end date is required")
    private LocalDateTime end;
}
