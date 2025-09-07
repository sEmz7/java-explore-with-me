package ru.yandex.practicum.ewmmainserver.model.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotNull
    @NotBlank
    @Email
    @Size(min = 6, max = 254)
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
