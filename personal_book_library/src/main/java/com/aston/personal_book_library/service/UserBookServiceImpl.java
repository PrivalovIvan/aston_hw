package com.aston.personal_book_library.service;

import com.aston.personal_book_library.domain.repository.UserBookRepository;
import com.aston.personal_book_library.domain.service.UserBookService;
import com.aston.personal_book_library.dto.UserBookDTO;
import com.aston.personal_book_library.mapper.UserBookMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class UserBookServiceImpl implements UserBookService {
    private final UserBookMapper userBookMapper = Mappers.getMapper(UserBookMapper.class);
    private final UserBookRepository userBookRepository;

    @Override
    public boolean addBookToUser(UUID userId, UUID bookId) {
        return userBookRepository.addBookToUser(userId, bookId);
    }

    @Override
    public void removeBookFromUser(UUID userId, UUID bookId) {
        userBookRepository.removeBookFromUser(userId, bookId);
    }

    @Override
    public List<UserBookDTO> getBooksByUserId(UUID userId) {
        return userBookRepository.getBooksByUserId(userId).stream()
                .map(userBookMapper::toUserBookDTO)
                .toList();
    }
}
