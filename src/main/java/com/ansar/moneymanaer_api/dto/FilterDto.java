package com.ansar.moneymanaer_api.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FilterDto {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String keyWord;
    private String sortField;
    private String sortOrder;
}
