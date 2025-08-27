package ru.yandex.practicum.ewmmainserver.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.ewmmainserver.service.impl.CategoryServiceImpl;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryRequestDto;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;
import ru.yandex.practicum.ewmmainserver.model.category.mapper.CategoryMapper;
import ru.yandex.practicum.ewmmainserver.repository.CategoryRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryRequestDto categoryRequestDto;
    private CategoryEntity categoryEntity;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        categoryRequestDto = new CategoryRequestDto("Концерты");
        categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Концерты");
        categoryResponseDto = new CategoryResponseDto(1L, "Концерты");
    }

    @Test
    void create() {
        when(categoryMapper.toEntity(any())).thenReturn(categoryEntity);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);
        when(categoryMapper.toDto(any())).thenReturn(categoryResponseDto);

        CategoryResponseDto responseDto = categoryService.createCategory(categoryRequestDto);

        assertNotNull(responseDto);
        assertThat(responseDto.getId(), equalTo(categoryResponseDto.getId()));
        assertThat(responseDto.getName(), equalTo(categoryResponseDto.getName()));

        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void updateCategory_whenCategoryExists_shouldUpdateAndReturnDto() {
        CategoryRequestDto updateDto = new CategoryRequestDto("newName");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toDto(any(CategoryEntity.class)))
                .thenAnswer(invocation -> {
                    CategoryEntity entity = invocation.getArgument(0);
                    return new CategoryResponseDto(entity.getId(), entity.getName());
                });

        CategoryResponseDto result = categoryService.updateCategory(updateDto, 1L);

        assertNotNull(result);
        assertEquals("newName", result.getName());
        assertEquals(1L, result.getId());
        assertEquals("newName", categoryEntity.getName());

        verify(categoryRepository).findById(1L);
        verify(categoryMapper).toDto(categoryEntity);
    }

    @Test
    void deleteCategory_whenCategoryExists_shouldDelete() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));

        categoryService.deleteCategory(1L);

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).deleteById(1L);
    }
}
