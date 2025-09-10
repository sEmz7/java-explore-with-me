package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.ewmmainserver.model.eventComment.dto.EventCommentResponseDto;
import ru.yandex.practicum.ewmmainserver.service.EventCommentService;

@Controller
@RequestMapping("/admin/events/comments")
@Validated
@RequiredArgsConstructor
public class EventCommentAdminController {
    private final EventCommentService commentService;

    @PatchMapping("/{commId}")
    public ResponseEntity<EventCommentResponseDto> updateCommentStatus(@Positive @PathVariable long commId,
                                                                       @RequestParam String status) {
        return ResponseEntity.ok(commentService.updateStatus(commId, status));
    }
}
