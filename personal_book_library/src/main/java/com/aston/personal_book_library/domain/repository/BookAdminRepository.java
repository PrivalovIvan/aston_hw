package com.aston.personal_book_library.domain.repository;

import com.aston.personal_book_library.domain.model.Book;

import java.util.UUID;

public interface BookAdminRepository extends BookCatalogRepository {
    void save(Book book);

    void deleteById(UUID id);

    void addGenreToBook(UUID bookId, UUID genreId);

    void removeGenreToBook(UUID bookId, UUID genreId);
}
