package ru.yandex.practicum.ewmmainserver.model.eventComment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainserver.model.eventComment.EventCommentEntity;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentRequestDto;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentResponseDto;

@Mapper(componentModel = "spring")
public interface EventCommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "status", ignore = true)
    EventCommentEntity toEntity(EventCommentRequestDto dto);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "eventId", source = "event.id")
    EventCommentResponseDto toDto(EventCommentEntity entity);
}
