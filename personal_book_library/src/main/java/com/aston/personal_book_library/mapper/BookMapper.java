package com.aston.personal_book_library.mapper;

import com.aston.personal_book_library.domain.model.Book;
import com.aston.personal_book_library.domain.model.Genre;
import com.aston.personal_book_library.dto.BookDTO;
import com.aston.personal_book_library.dto.GenreDTO;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper
public interface BookMapper {
    Book toBook(BookDTO bookDTO);

    BookDTO toBookDTO(Book book);

    Set<GenreDTO> toGenreDTOSet(Set<Genre> genres);

    Set<Genre> toGenreSet(Set<GenreDTO> genreDTOs);
}
