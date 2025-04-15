package com.aston.personal_book_library.domain.repository;

import com.aston.personal_book_library.domain.model.UserBook;

import java.util.List;
import java.util.UUID;

public interface UserBookRepository {
    boolean addBookToUser(UUID userId, UUID bookId);

    void removeBookFromUser(UUID userId, UUID bookId);

    List<UserBook> getBooksByUserId(UUID userId);
}
