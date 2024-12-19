package ru.practicum.main.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    private String email;
}
