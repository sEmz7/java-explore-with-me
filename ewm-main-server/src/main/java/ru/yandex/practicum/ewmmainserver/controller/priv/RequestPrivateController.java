package ru.yandex.practicum.ewmmainserver.controller.priv;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.RequestDto;
import ru.yandex.practicum.ewmmainserver.service.RequestService;

import java.util.List;

@Controller
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
@Validated
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDto>> getUserRequests(@PositiveOrZero @PathVariable long userId) {
        return ResponseEntity.ok(requestService.getUserRequests(userId));
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@PositiveOrZero @PathVariable long userId,
                                                    @PositiveOrZero @RequestParam long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelUserRequest(@PositiveOrZero @PathVariable long userId,
                                                        @PositiveOrZero @PathVariable long requestId) {
        return ResponseEntity.ok(requestService.cancelUserRequest(userId, requestId));
    }
}
