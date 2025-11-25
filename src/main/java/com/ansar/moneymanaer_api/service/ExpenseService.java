package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.ExpenseDTO;
import com.ansar.moneymanaer_api.entity.CategoryEntity;
import com.ansar.moneymanaer_api.entity.ExpenseEntity;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.exception.ResourceNotFoundException;
import com.ansar.moneymanaer_api.repository.CategoryRepository;
import com.ansar.moneymanaer_api.repository.ExpenseRepository;
import com.ansar.moneymanaer_api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {

        ProfileEntity profile = profileService.getCurrentProfile();

        if (expenseDTO.getCategoryId() == null || expenseDTO.getAmount() == null) {
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

    //Retrieves the all expenses for current month/based on the start date and end date
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {

        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate date = LocalDate.now();
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.getDayOfMonth());
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(MapperUtil::expenseEntityToDto).collect(Collectors.toList());
    }

    //delete expense by id for current user
    public void deleteExpense(Long expenseId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity expenseEntity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> {
                    log.error("Expense not found {}", expenseId);
                    return new ResourceNotFoundException("Expense not found with id: " + expenseId);
                });
        if(!expenseEntity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorizes to delete this request");
        }
        expenseRepository.delete(expenseEntity);
    }
     //get latest top 5 expense current user
    public List<ExpenseDTO> getLatestTopFiveExpensesCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> expenseEntityList = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return expenseEntityList.stream().map(MapperUtil::expenseEntityToDto).collect(Collectors.toList());
    }
    //get total expense for current user
    public BigDecimal getTotalExpensesCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total !=null?total:BigDecimal.ZERO;
    }

    //filter expense
    public List<ExpenseDTO> filterExpense(LocalDate startDate, LocalDate endDate, String keyWord, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate,keyWord, sort);
        return list.stream().map(MapperUtil::expenseEntityToDto).toList();
    }

}
