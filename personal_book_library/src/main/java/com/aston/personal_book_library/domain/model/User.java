package com.aston.personal_book_library.domain.model;

import com.aston.personal_book_library.common.annotation.Role;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class User {
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;
    private final Role role;
}
