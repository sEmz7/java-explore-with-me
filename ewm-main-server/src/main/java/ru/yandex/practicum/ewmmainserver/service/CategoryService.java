package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryRequestDto;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto dto);

    void deleteCategory(long catId);

    CategoryResponseDto updateCategory(CategoryRequestDto dto, long catId);

    List<CategoryResponseDto> getAllCategories(int from, int size);

    CategoryResponseDto getCategoryById(long catId);
}
