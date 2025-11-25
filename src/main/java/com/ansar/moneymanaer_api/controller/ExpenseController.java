package com.ansar.moneymanaer_api.controller;

import com.ansar.moneymanaer_api.dto.ExpenseDTO;
import com.ansar.moneymanaer_api.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody  ExpenseDTO expenseDTO) {
        ExpenseDTO expenseDto = expenseService.addExpense(expenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseDto);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getExpenses(){
        return ResponseEntity.ok(expenseService.getCurrentMonthExpensesForCurrentUser());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();

    }
}
