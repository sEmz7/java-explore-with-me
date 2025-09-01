package ru.yandex.practicum.ewmmainserver.model.participationRequest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.RequestEntity;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.RequestDto;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    RequestDto toDto(RequestEntity entity);
}
