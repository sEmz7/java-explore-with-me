package ru.yandex.practicum.ewmmainserver.model.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryRequestDto;
import ru.yandex.practicum.ewmmainserver.model.category.dto.CategoryResponseDto;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    CategoryEntity toEntity(CategoryRequestDto dto);

    CategoryResponseDto toDto(CategoryEntity entity);
}
