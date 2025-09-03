package ru.yandex.practicum.ewmmainserver.model.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;

    private List<EventShortDto> events;

    private Boolean pinned;

    private String title;
}
