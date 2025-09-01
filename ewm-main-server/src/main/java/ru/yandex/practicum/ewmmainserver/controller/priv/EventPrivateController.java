package ru.yandex.practicum.ewmmainserver.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.event.dto.NewEventDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.UpdateEventDto;
import ru.yandex.practicum.ewmmainserver.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAllEventsByUser(@Min(1) @PathVariable long userId,
                                                                 @RequestParam(defaultValue = "0") int from,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventService.getAllByUser(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@Valid @RequestBody NewEventDto dto,
                                                    @Min(1) @PathVariable long userId) {
        EventFullDto createdDto = eventService.create(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventByIdAndUser(@Min(1) @PathVariable long userId,
                                                     @Min(1) @PathVariable long eventId) {
        return ResponseEntity.ok(eventService.getById(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@Valid @RequestBody UpdateEventDto dto,
                                                    @Min(1) @PathVariable long userId,
                                                    @Min(1) @PathVariable long eventId) {
        return ResponseEntity.ok(eventService.update(dto, userId, eventId));
    }
}
