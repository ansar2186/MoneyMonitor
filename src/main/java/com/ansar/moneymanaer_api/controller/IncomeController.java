package com.ansar.moneymanaer_api.controller;

import com.ansar.moneymanaer_api.dto.IncomeDto;
import com.ansar.moneymanaer_api.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto incomeDto) {
        IncomeDto dto = incomeService.addIncome(incomeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDto>> getExpenses() {
        return ResponseEntity.ok(incomeService.getCurrentMonthIncomeForCurrentUser());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncome(@PathVariable Long id){
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();

    }
}
