package ru.yandex.practicum.ewmmainserver.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserRequestDto;
import ru.yandex.practicum.ewmmainserver.model.user.dto.UserResponseDto;
import ru.yandex.practicum.ewmmainserver.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UsersAdminController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @PositiveOrZero @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PositiveOrZero @PathVariable long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
