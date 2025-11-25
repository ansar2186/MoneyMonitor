package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.ExpenseDTO;
import com.ansar.moneymanaer_api.dto.IncomeDto;
import com.ansar.moneymanaer_api.entity.CategoryEntity;
import com.ansar.moneymanaer_api.entity.ExpenseEntity;
import com.ansar.moneymanaer_api.entity.IncomeEntity;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.exception.ResourceNotFoundException;
import com.ansar.moneymanaer_api.repository.CategoryRepository;
import com.ansar.moneymanaer_api.repository.ExpenseRepository;
import com.ansar.moneymanaer_api.repository.IncomeRepository;
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
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;
    private final ExpenseRepository expenseRepository;

    public IncomeDto addIncome(IncomeDto incomeDto) {

        ProfileEntity profile = profileService.getCurrentProfile();

        log.info("Profile Id: {}", profile.getEmail());

        CategoryEntity categoryEntity = categoryRepository.findById(incomeDto.getCategoryId())
                .orElseThrow(() -> {
                    log.error("Category not found {}", incomeDto.getCategoryName());
                    return new ResourceNotFoundException("Category not found");
                });

        IncomeEntity incomeEntity = MapperUtil.dtoToIncomeEntity(incomeDto, profile, categoryEntity);
        IncomeEntity savedIncome = incomeRepository.save(incomeEntity);

        log.info("Saved Income {}", savedIncome);

        return MapperUtil.incomeEntityToDto(savedIncome);
    }

    //Retrieves the all expenses for current month/based on the start date and end date
    public List<IncomeDto> getCurrentMonthIncomeForCurrentUser() {

        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate date = LocalDate.now();
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.getDayOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);

        return list.stream().map(MapperUtil::incomeEntityToDto).collect(Collectors.toList());
    }

    //delete income by id for current user
    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity incomeEntity = incomeRepository.findById(incomeId)
                .orElseThrow(() -> {
                    log.error("Income not found {}", incomeId);
                    return new ResourceNotFoundException("Income not found with id: " + incomeId);
                });
        if(!incomeEntity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorizes to delete this request");
        }
        incomeRepository.delete(incomeEntity);
    }

    //get latest top 5 income current user
    public List<IncomeDto> getLatestTopFiveIncomeCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdOrderByDateDesc(profile.getId());
        return incomeEntities.stream().map(MapperUtil::incomeEntityToDto).collect(Collectors.toList());
    }
    //get total income for current user
    public BigDecimal getTotalIncomeCurrentUser(){
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalIncomeByProfileId(profile.getId());
        return total !=null?total:BigDecimal.ZERO;
    }

    //filter incomes
    public List<IncomeDto> filterIncome(LocalDate startDate, LocalDate endDate, String keyWord, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate,keyWord, sort);
        return list.stream().map(MapperUtil::incomeEntityToDto).toList();
    }


}
