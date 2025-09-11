package ru.yandex.practicum.ewmmainserver.model.eventComment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.ewmmainserver.model.event.EventState;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCommentResponseDto {
    private Long id;
    private Long eventId;
    private Long userId;
    private String text;
    private LocalDateTime createdOn;
    private EventState status;
}
