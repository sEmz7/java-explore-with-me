package ru.yandex.practicum.ewmmainserver.model.eventComment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCommentRequestDto {
    @NotBlank
    @Size(min = 2, max = 2000)
    private String text;
}
