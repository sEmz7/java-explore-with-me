package ru.yandex.practicum.ewmmainserver.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.ewmmainserver.exception.ConflictException;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryRequestDto;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;
import ru.yandex.practicum.ewmmainserver.model.category.mapper.CategoryMapper;
import ru.yandex.practicum.ewmmainserver.repository.CategoryRepository;
import ru.yandex.practicum.ewmmainserver.exception.NotFoundException;
import ru.yandex.practicum.ewmmainserver.repository.EventRepository;
import ru.yandex.practicum.ewmmainserver.service.CategoryService;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        if (categoryRepository.findByName(dto.getName()).isPresent()) {
            throw new ConflictException("Категория с именем " + dto.getName() + " уже существует");
        }
        CategoryEntity category = categoryMapper.toEntity(dto);
        CategoryEntity savedCategory = categoryRepository.save(category);
        log.debug("Категория с id={} и name={} была создана.", savedCategory.getId(), savedCategory.getName());
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public void deleteCategory(long catId) {
        findCategoryByIdOrThrow(catId);
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("Категория используется в событиях и не может быть удалена");
        }
        categoryRepository.deleteById(catId);
        log.debug("Категория с id={} удалена.", catId);
    }

    @Override
    public CategoryResponseDto updateCategory(CategoryRequestDto dto, long catId) {
        CategoryEntity category = findCategoryByIdOrThrow(catId);
        if (categoryRepository.findByName(dto.getName()).isPresent() && !dto.getName().equals(category.getName())) {
            throw new ConflictException("Категория с именем " + dto.getName() + " уже существует");
        }
        category.setName(dto.getName());
        log.debug("У категории с id={} изменено поле name на '{}'", category.getId(), category.getName());
        return categoryMapper.toDto(category);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryResponseDto> getAllCategories(int from, int size) {
        return categoryRepository.findAllFromBySize(from, size)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponseDto getCategoryById(long catId) {
        return categoryMapper.toDto(findCategoryByIdOrThrow(catId));
    }

    private CategoryEntity findCategoryByIdOrThrow(long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> {
            log.warn("Категория с id={} не найдена.", catId);
            return new NotFoundException("Категория с id=" + catId + " не найдена");
        });
    }
}
