package com.aston.personal_book_library.domain.model;

import com.aston.personal_book_library.common.annotation.ReadStatus;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class UserBook {
    private final UUID id;
    private final Author author;
    private final Book book;
    private final int rating;
    private final ReadStatus readStatus;
}
