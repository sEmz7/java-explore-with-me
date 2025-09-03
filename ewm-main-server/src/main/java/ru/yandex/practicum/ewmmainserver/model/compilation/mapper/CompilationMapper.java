package ru.yandex.practicum.ewmmainserver.model.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.yandex.practicum.ewmmainserver.model.compilation.CompilationEntity;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.CompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.NewCompilationDto;
import ru.yandex.practicum.ewmmainserver.model.compilation.dto.UpdateCompilationDto;
import ru.yandex.practicum.ewmmainserver.model.event.mapper.EventMapper;

@Mapper(componentModel = "spring", uses = EventMapper.class)
public interface CompilationMapper {

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", ignore = true)
    CompilationEntity toEntity(NewCompilationDto dto);

    CompilationDto toDto(CompilationEntity entity);

    @Mapping(target = "events", ignore = true)
    @Mapping(target = "id", ignore = true)
    CompilationEntity updateEntity(UpdateCompilationDto updateDto, @MappingTarget CompilationEntity entity);
}
