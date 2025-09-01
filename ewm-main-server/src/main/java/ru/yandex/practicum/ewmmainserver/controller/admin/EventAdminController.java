package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.event.dto.EventFullDto;
import ru.yandex.practicum.ewmmainserver.model.event.dto.UpdateEventDto;
import ru.yandex.practicum.ewmmainserver.service.EventService;

@RestController
@RequestMapping("/admin/events/{eventId}")
@RequiredArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @PatchMapping
    public ResponseEntity<EventFullDto> editEventStatus(@Valid @RequestBody UpdateEventDto dto,
                                                        @Min(1) @PathVariable long eventId) {
        return ResponseEntity.ok(eventService.editStatus(dto, eventId));
    }
}
