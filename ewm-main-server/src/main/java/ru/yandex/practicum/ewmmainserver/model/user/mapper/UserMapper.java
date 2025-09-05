package ru.yandex.practicum.ewmmainserver.model.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserRequestDto;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserRequestDto dto);

    UserResponseDto toDto(UserEntity entity);
}
