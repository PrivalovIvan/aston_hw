package com.aston.personal_book_library.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class Author {
    private final UUID id;
    private final String name;
    private final String country;
}
