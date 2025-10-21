package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.ExpenseDTO;
import com.ansar.moneymanaer_api.entity.CategoryEntity;
import com.ansar.moneymanaer_api.entity.ExpenseEntity;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.exception.ResourceNotFoundException;
import com.ansar.moneymanaer_api.repository.CategoryRepository;
import com.ansar.moneymanaer_api.repository.ExpenseRepository;
import com.ansar.moneymanaer_api.repository.ProfileRepository;
import com.ansar.moneymanaer_api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {

        ProfileEntity profile = profileService.getCurrentProfile();

        if(expenseDTO.getCategoryId()==null || expenseDTO.getAmount()==null){
            log.error("Category Id and Amount are required");
            throw new IllegalArgumentException("Category Id and Amount are required");
        }

        log.info("Profile Id: {}", profile.getEmail());

        CategoryEntity categoryEntity = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category not found {}", expenseDTO.getCategoryName());
                    return new ResourceNotFoundException("Category not found");
                });

        ExpenseEntity expenseEntity = MapperUtil.dtoToExpenseEntity(expenseDTO, profile, categoryEntity);
        ExpenseEntity savedExpense = expenseRepository.save(expenseEntity);

        log.info("Expense saved successfully with id: {}, Amount: {} ,Category: {}", savedExpense.getId(), savedExpense.getAmount(), savedExpense.getCategory().getName());

        return MapperUtil.expenseEntityToDto(savedExpense);
    }

}
