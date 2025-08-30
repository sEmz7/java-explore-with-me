package ru.yandex.practicum.ewmmainserver.model.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserShortDto;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "locationLat", source = "location.lat")
    @Mapping(target = "locationLon", source = "location.lon")
    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "state", ignore = true)
    EventEntity toEntity(NewEventDto dto);

    @Mapping(target = "location.lat", source = "locationLat")
    @Mapping(target = "location.lon", source = "locationLon")
    EventFullDto toDto(EventEntity entity);

    default CategoryResponseDto toDto(CategoryEntity entity) {
        if (entity == null) return null;
        return new CategoryResponseDto(entity.getId(), entity.getName());
    }

    default UserShortDto toDto(UserEntity entity) {
        if (entity == null) return null;
        return new UserShortDto(entity.getId(), entity.getName());
    }
}
