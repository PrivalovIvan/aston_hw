package com.aston.personal_book_library.dto;

import com.aston.personal_book_library.common.annotation.ReadStatus;
import com.aston.personal_book_library.domain.model.Author;
import com.aston.personal_book_library.domain.model.Book;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class UserBookDTO {
    private final UUID id;
    private final Author author;
    private final Book book;
    private final int rating;
    private final ReadStatus readStatus;
}

