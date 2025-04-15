package com.aston.personal_book_library.domain.service;

import com.aston.personal_book_library.dto.BookDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookCatalogService {
    List<BookDTO> findAll();

    Optional<BookDTO> findById(UUID id);

    Optional<BookDTO> findByTitle(String title);

    List<BookDTO> findByAuthor(UUID authorId);

    List<BookDTO> findByGenre(String genre);

    List<BookDTO> findByYearRange(int startYear, int endYear);

    List<BookDTO> findByTitleContaining(String keyword);
}
