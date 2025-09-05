package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.UpdateEventDto;
import ru.yandex.practicum.ewmmainserver.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> searchEvents(@RequestParam(required = false) List<Long> users,
                                                           @RequestParam(required = false)  List<String> states,
                                                           @RequestParam(required = false)  List<Long> categories,
                                                           @RequestParam(required = false)
                                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                               LocalDateTime rangeStart,
                                                           @RequestParam(required = false)
                                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                               LocalDateTime rangeEnd,
                                                           @RequestParam(defaultValue = "0") int from,
                                                           @RequestParam(defaultValue = "10") int size) {
        List<EventFullDto> dtos = eventService.searchEvents(
                users, states, categories, rangeStart, rangeEnd, from, size
        );
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> editEventStatus(@Valid @RequestBody UpdateEventDto dto,
                                                        @PositiveOrZero @PathVariable long eventId) {
        return ResponseEntity.ok(eventService.editStatus(dto, eventId));
    }
}
