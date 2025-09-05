package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryRequestDto;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;
import ru.yandex.practicum.ewmmainserver.service.CategoryService;

@Controller
@RequestMapping(("/admin/categories"))
@RequiredArgsConstructor
@Validated
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto dto) {
        CategoryResponseDto responseDto = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PositiveOrZero @PathVariable long catId) {
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @Valid @RequestBody CategoryRequestDto dto,
            @PositiveOrZero @PathVariable long catId) {
        CategoryResponseDto responseDto = categoryService.updateCategory(dto, catId);
        return ResponseEntity.ok(responseDto);
    }
}
