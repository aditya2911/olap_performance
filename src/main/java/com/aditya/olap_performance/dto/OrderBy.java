package com.aditya.olap_performance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBy {

    @NotBlank(message = "orderBy field is required")
    private String field;

    @NotBlank(message = "orderBy direction is required")
    @Pattern(regexp = "ASC|DESC", message = "direction must be ASC or DESC")
    @Builder.Default
    private String direction = "DESC";
}
