package ru.yandex.practicum.ewmmainserver.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentRequestDto;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentResponseDto;
import ru.yandex.practicum.ewmmainserver.service.EventCommentService;

import java.util.List;

@Controller
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
@Valid
public class EventCommentPrivateController {
    private final EventCommentService eventCommentService;

    @PostMapping
    public ResponseEntity<EventCommentResponseDto> createComment(@Positive @PathVariable long userId,
                                                                 @Positive @PathVariable long eventId,
                                                                 @Valid @RequestBody EventCommentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventCommentService.create(userId, eventId, dto));
    }

    @PatchMapping("/{commId}")
    public ResponseEntity<EventCommentResponseDto> updateComment(@Positive @PathVariable long userId,
                                                                 @Positive @PathVariable long eventId,
                                                                 @Positive @PathVariable long commId,
                                                                 @Valid @RequestBody EventCommentRequestDto dto) {
        return ResponseEntity.ok(eventCommentService.update(userId, eventId, commId, dto));
    }

    @DeleteMapping("/{commId}")
    public ResponseEntity<Void> deleteComment(@Positive @PathVariable long userId,
                                              @Positive @PathVariable long eventId,
                                              @Positive @PathVariable long commId) {
        eventCommentService.delete(userId, eventId, commId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<EventCommentResponseDto>> getComments(@Positive @PathVariable long userId,
                                                                     @Positive @PathVariable long eventId,
                                                                     @RequestParam(defaultValue = "0") int from,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(eventCommentService.getCommentsByEvent(userId, eventId, from, size));
    }

    @GetMapping("/{commId}")
    public ResponseEntity<EventCommentResponseDto> getComment(@Positive @PathVariable long userId,
                                                              @Positive @PathVariable long eventId,
                                                              @Positive @PathVariable long commId) {
        return ResponseEntity.ok(eventCommentService.getById(userId, eventId, commId));
    }
}
