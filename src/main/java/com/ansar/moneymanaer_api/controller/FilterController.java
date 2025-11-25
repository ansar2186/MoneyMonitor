package com.ansar.moneymanaer_api.controller;

import com.ansar.moneymanaer_api.dto.FilterDto;
import com.ansar.moneymanaer_api.service.ExpenseService;
import com.ansar.moneymanaer_api.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDto filterDto) {

        LocalDate startDate = Optional.ofNullable(filterDto.getStartDate()).orElse(LocalDate.MIN);
        LocalDate endDate = Optional.ofNullable(filterDto.getEndDate()).orElse(LocalDate.now());
        String keyWord = Optional.ofNullable(filterDto.getKeyWord()).orElse("");
        String sortField = Optional.ofNullable(filterDto.getSortField()).orElse("date");
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDto.getSortOrder()) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        if ("income".equalsIgnoreCase(filterDto.getType())) {
            return ResponseEntity.ok(incomeService.filterIncome(startDate, endDate, keyWord, sort));
        } else if ("expense".equalsIgnoreCase(filterDto.getType())) {
            return ResponseEntity.ok(expenseService.filterExpense(startDate, endDate, keyWord, sort));
        } else {
            return ResponseEntity.badRequest().body("Invalid type .Must be 'income' or 'expense'");
        }
    }
}