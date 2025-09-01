package ru.yandex.practicum.ewmmainserver.controller.priv;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.dto.RequestDto;
import ru.yandex.practicum.ewmmainserver.service.RequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<RequestDto>> getUserRequests(@Min(1) @PathVariable long userId) {
        return ResponseEntity.ok(requestService.getUserRequests(userId));
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@Min(1) @PathVariable long userId,
                                                    @Min(1) @RequestParam long eventId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelUserRequest(@Min(1) @PathVariable long userId,
                                                        @Min(1) @PathVariable long requestId) {
        return ResponseEntity.ok(requestService.cancelUserRequest(userId, requestId));
    }
}
