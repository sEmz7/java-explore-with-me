package ru.yandex.practicum.ewmmainserver.controller.pub;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;
import ru.yandex.practicum.ewmmainserver.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(@RequestParam(defaultValue = "0") int from,
                                                                      @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(categoryService.getAllCategories(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@Min(1) @PathVariable long catId) {
        return ResponseEntity.ok(categoryService.getCategoryById(catId));
    }
}
