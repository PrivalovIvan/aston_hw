package com.aston.personal_book_library.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class Book {
    private final UUID id;
    private final String title;
    private final int publicationYear;
    private final UUID authorId;
    private final Set<Genre> genres = new HashSet<>();
}
