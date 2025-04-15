package com.aston.personal_book_library.service;

import com.aston.personal_book_library.domain.repository.BookCatalogRepository;
import com.aston.personal_book_library.domain.service.BookCatalogService;
import com.aston.personal_book_library.dto.BookDTO;
import com.aston.personal_book_library.mapper.BookMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BookCatalogServiceImpl implements BookCatalogService {
    private final BookCatalogRepository bookCatalogRepository;
    private final BookMapper bookMapper;
//    private final BookMapper bookMapper = Mappers.getMapper(BookMapper.class);


    @Override
    public List<BookDTO> findAll() {
        return bookCatalogRepository.getAll().stream().map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        }).toList();
    }

    @Override
    public Optional<BookDTO> findById(UUID id) {
        return bookCatalogRepository.getById(id).map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        });
    }

    @Override
    public Optional<BookDTO> findByTitle(String title) {
        return bookCatalogRepository.getByTitle(title).map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        });
    }

    @Override
    public List<BookDTO> findByAuthor(UUID authorId) {
        return bookCatalogRepository.getByAuthor(authorId).stream().map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        }).toList();
    }

    @Override
    public List<BookDTO> findByGenre(String genre) {
        return bookCatalogRepository.getByGenre(genre).stream().map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        }).toList();
    }

    @Override
    public List<BookDTO> findByYearRange(int startYear, int endYear) {
        return bookCatalogRepository.getByYearRange(startYear, endYear).stream().map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        }).toList();
    }

    @Override
    public List<BookDTO> findByTitleContaining(String keyword) {
        return bookCatalogRepository.getByTitleContaining(keyword).stream().map(book -> {
            BookDTO bookDTO = bookMapper.toBookDTO(book);
            bookDTO.getGenres().addAll(bookMapper.toGenreDTOSet(book.getGenres()));
            return bookDTO;
        }).toList();
    }
}
