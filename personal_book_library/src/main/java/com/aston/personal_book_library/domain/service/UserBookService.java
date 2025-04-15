package com.aston.personal_book_library.domain.service;

import com.aston.personal_book_library.dto.UserBookDTO;

import java.util.List;
import java.util.UUID;

public interface UserBookService {
    boolean addBookToUser(UUID userId, UUID bookId);

    void removeBookFromUser(UUID userId, UUID bookId);

    List<UserBookDTO> getBooksByUserId(UUID userId);
}
