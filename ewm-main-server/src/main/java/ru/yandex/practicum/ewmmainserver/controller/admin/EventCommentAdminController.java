package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentResponseDto;
import ru.yandex.practicum.ewmmainserver.service.EventCommentService;

@Controller
@RequestMapping("/admin/events/comments")
@Valid
@RequiredArgsConstructor
public class EventCommentAdminController {
    private final EventCommentService commentService;

    @PatchMapping("/{commId}")
    public ResponseEntity<EventCommentResponseDto> updateCommentStatus(@Positive @PathVariable long commId,
                                                                       @RequestParam String status) {
        return ResponseEntity.ok(commentService.updateStatus(commId, status));
    }
}
