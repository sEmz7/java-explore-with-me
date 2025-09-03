package ru.yandex.practicum.ewmmainserver.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewmmainserver.exception.NotFoundException;
import ru.yandex.practicum.ewmmainserver.model.compilation.CompilationEntity;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.CompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.mapper.CompilationMapper;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.repository.CompilationRepository;
import ru.yandex.practicum.ewmmainserver.repository.EventRepository;
import ru.yandex.practicum.ewmmainserver.service.CompilationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto create(NewCompilationDto newDto) {
        CompilationEntity compilation = compilationMapper.toEntity(newDto);
        Set<EventEntity> events = new HashSet<>();
        if (newDto.getEvents() != null) {
            newDto.getEvents().forEach(compId -> {
                EventEntity event = eventRepository.findById(compId)
                        .orElseThrow(() -> new NotFoundException("Не найдено событие с id=" + compId));
                events.add(event);
            });
            compilation.setEvents(events);
        }
        CompilationEntity saved = compilationRepository.save(compilation);
        return compilationMapper.toDto(saved);
    }

    @Override
    public void delete(long compId) {
        findCompilationByIdOrThrow(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto update(UpdateCompilationDto updateDto, long compId) {
        CompilationEntity compilation = findCompilationByIdOrThrow(compId);
        compilationMapper.updateEntity(updateDto, compilation);
        if (updateDto.getEvents() != null) {
            List<EventEntity> events = eventRepository.findAllById(updateDto.getEvents());
            if (events.size() != updateDto.getEvents().size()) {
                throw new NotFoundException("Одно или более событий не найдено");
            }
            compilation.setEvents(new HashSet<>(events));
        }
        compilation = compilationRepository.save(compilation);
        return compilationMapper.toDto(compilation);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(0, from + size, Sort.by("id"));
        List<CompilationEntity> compilations = compilationRepository.findCompilations(pinned, pageable).getContent();
        return compilations.stream()
                .skip(from)
                .limit(size)
                .map(compilationMapper::toDto)
                .toList();
    }

    @Override
    public CompilationDto getById(long compId) {
        return compilationMapper.toDto(findCompilationByIdOrThrow(compId));
    }

    private CompilationEntity findCompilationByIdOrThrow(long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдено событие с id=" + compId));
    }
}
