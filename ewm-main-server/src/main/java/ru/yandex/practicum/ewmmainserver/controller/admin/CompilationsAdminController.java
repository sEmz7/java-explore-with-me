package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.CompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.ewmmainserver.service.CompilationService;

@Controller
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Validated
public class CompilationsAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto newDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(compilationService.create(newDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PositiveOrZero @PathVariable long compId) {
        compilationService.delete(compId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@Valid @RequestBody UpdateCompilationDto updateDto,
                                                            @PositiveOrZero @PathVariable long compId) {
        return ResponseEntity.ok(compilationService.update(updateDto, compId));
    }
}
