package com.aston.personal_book_library.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class BookDTO {
    private final UUID id;
    private final String title;
    private final int publicationYear;
    private final UUID authorId;
    @Singular
    private final Set<GenreDTO> genres = new HashSet<>();
}