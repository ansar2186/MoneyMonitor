package com.ansar.moneymanaer_api.service;

import com.ansar.moneymanaer_api.dto.CategoryDto;
import com.ansar.moneymanaer_api.entity.CategoryEntity;
import com.ansar.moneymanaer_api.entity.ProfileEntity;
import com.ansar.moneymanaer_api.exception.CategoryAlreadyExistException;
import com.ansar.moneymanaer_api.repository.CategoryRepository;
import com.ansar.moneymanaer_api.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProfileService profileService;

    public CategoryDto saveCategory(CategoryDto categoryDto) {
        ProfileEntity currentProfile = profileService.getCurrentProfile();
        log.info("Current Profile : {}", currentProfile.getEmail());
        if (categoryRepository.existsByNameAndProfileId(categoryDto.getName(), currentProfile.getId())) {
            log.info("Category already exists : {}", categoryDto.getName());
            throw new CategoryAlreadyExistException("Category already exists");
        }
        CategoryEntity categoryEntity = MapperUtil.dtoToCategoryEntity(categoryDto, currentProfile);
        categoryEntity = categoryRepository.save(categoryEntity);
        log.info("Saved Category successfully : {}", categoryEntity.getName());
        return MapperUtil.categoryEntityToDto(categoryEntity);
    }

    public List<CategoryDto> getCategoriesForCurrentProfile() {
        ProfileEntity profile = profileService.getCurrentProfile();
        var categories = categoryRepository.findByProfileId(profile.getId());
        return categories.stream().map(MapperUtil::categoryEntityToDto).toList();
    }

    public List<CategoryDto> getCategoriesByTypeForCurrentUser(String type) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<CategoryEntity> category = categoryRepository.findByTypeAndProfileId(type, profile.getId());
        return category.stream().map(MapperUtil::categoryEntityToDto).toList();
    }

    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository.findByIdAndProfileId(categoryId, profile.getId())
                .orElseThrow(() -> {
                    log.info("Category id {} not found", categoryId);
                    return new RuntimeException("Category not found with Category id : " + categoryId);
                });
        categoryEntity.setName(categoryDto.getName());
        categoryEntity.setIcon(categoryDto.getIcon());
        categoryEntity = categoryRepository.save(categoryEntity);
        return MapperUtil.categoryEntityToDto(categoryEntity);

    }
}
