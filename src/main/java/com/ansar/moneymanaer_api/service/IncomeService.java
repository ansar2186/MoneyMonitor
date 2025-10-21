package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.IncomeDto;
import com.ansar.moneymanaer_api.entity.CategoryEntity;
import com.ansar.moneymanaer_api.entity.IncomeEntity;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.exception.ResourceNotFoundException;
import com.ansar.moneymanaer_api.repository.CategoryRepository;
import com.ansar.moneymanaer_api.repository.IncomeRepository;
import com.ansar.moneymanaer_api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

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
}
