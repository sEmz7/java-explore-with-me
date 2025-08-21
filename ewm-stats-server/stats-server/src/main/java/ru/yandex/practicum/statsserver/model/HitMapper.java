package ru.yandex.practicum.statsserver.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.statsdto.HitDto;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface HitMapper {

    @Mapping(source = "hitDate", target = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    HitDto toDto(Hit entity);

    @Mapping(source = "timestamp", target = "hitDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Hit toEntity(HitDto dto);
}
