package ru.yandex.practicum.ewmmainserver.service;

import ru.yandex.practicum.ewmmainserver.model.compilation.dto.CompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newDto);

    void delete(long compId);

    CompilationDto update(UpdateCompilationDto updateDto, long compId);

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getById(long compId);
}
