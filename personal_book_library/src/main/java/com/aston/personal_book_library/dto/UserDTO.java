package com.aston.personal_book_library.dto;

import com.aston.personal_book_library.common.annotation.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
@RequiredArgsConstructor
public class UserDTO {
    private final UUID id;
    @NotBlank(message = "Enter your name")
    private final String username;
    @NotBlank(message = "Enter your email")
    @Email(message = "Enter correct email")
    private final String email;
    @NotBlank(message = "Enter password")
    private final String password;
    private final Role role;

    @Override
    public String toString() {
        return """
                {
                    "Profile": {
                        "email": "%s",
                        "role": "%s",
                        "username": "%s",
                        "id": "%s"
                    }
                }
                """.formatted(email, role, username, id);
    }
}
