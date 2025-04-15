package com.aston.personal_book_library.domain.repository;

import com.aston.personal_book_library.domain.model.Book;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookCatalogRepository {
    List<Book> getAll();

    Optional<Book> getById(UUID id);

    Optional<Book> getByTitle(String title);

    List<Book> getByAuthor(UUID authorId);

    List<Book> getByGenre(String genre);

    List<Book> getByYearRange(int startYear, int endYear);

    List<Book> getByTitleContaining(String keyword);

}
