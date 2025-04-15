package com.aston.personal_book_library.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class AuthorDTO {
    private final UUID id;
    private final String name;
    private final String country;
}
