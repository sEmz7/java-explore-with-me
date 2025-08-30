package ru.yandex.practicum.ewmmainserver.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.service.EventService;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@Valid @RequestBody NewEventDto dto,
                                                    @Min(1) @PathVariable long userId) {
        EventFullDto createdDto = eventService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }
}
