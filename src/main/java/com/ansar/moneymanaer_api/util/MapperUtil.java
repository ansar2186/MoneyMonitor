package com.ansar.moneymanaer_api.util;

import com.ansar.moneymanaer_api.dto.CategoryDto;
import com.ansar.moneymanaer_api.dto.ExpenseDTO;
import com.ansar.moneymanaer_api.dto.IncomeDto;
import com.ansar.moneymanaer_api.dto.ProfileDto;
import com.ansar.moneymanaer_api.entity.CategoryEntity;
import com.ansar.moneymanaer_api.entity.ExpenseEntity;
import com.ansar.moneymanaer_api.entity.IncomeEntity;
import com.ansar.moneymanaer_api.entity.ProfileEntity;

public class MapperUtil {

    public static ProfileEntity dtoToProfileEntity(ProfileDto profileDto) {
        return ProfileEntity.builder()
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }

    public static ProfileDto profileEntityToDto(ProfileEntity profileEntity) {
        return ProfileDto.builder()
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }

    public static CategoryEntity dtoToCategoryEntity(CategoryDto categoryDto, ProfileEntity profileEntity) {
        return CategoryEntity.builder()
                .name(categoryDto.getName())
                .icon(categoryDto.getIcon())
                .profile(profileEntity)
                .type(categoryDto.getType())
                .build();
    }

    public static CategoryDto categoryEntityToDto(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .profileId(entity.getProfile() != null ? entity.getProfile().getId() : null)
                .name(entity.getName())
                .icon(entity.getIcon())
                .type(entity.getType())
                .createdDate(entity.getCreatedDate())
                .updatedDate(entity.getUpdatedDate())
                .build();
    }

    public static ExpenseEntity dtoToExpenseEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .icon(expenseDTO.getIcon())
                .amount(expenseDTO.getAmount())
                .date(expenseDTO.getDate())
                .profile(profileEntity)
                .category(categoryEntity)
                .build();
    }

    public static ExpenseDTO expenseEntityToDto(ExpenseEntity entity) {
        return ExpenseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .created(entity.getCreatedAt())
                .updated(entity.getUpdatedAt())
                .build();

    }

    public static IncomeEntity dtoToIncomeEntity(IncomeDto IncomeDto, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return IncomeEntity.builder()
                .name(IncomeDto.getName())
                .icon(IncomeDto.getIcon())
                .amount(IncomeDto.getAmount())
                .date(IncomeDto.getDate())
                .profile(profileEntity)
                .category(categoryEntity)
                .build();
    }

    public static IncomeDto incomeEntityToDto(IncomeEntity entity) {
        return IncomeDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .created(entity.getCreatedAt())
                .updated(entity.getUpdatedAt())
                .build();

    }
}
