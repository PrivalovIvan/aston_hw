package com.aston.personal_book_library.mapper;

import com.aston.personal_book_library.domain.model.Genre;
import com.aston.personal_book_library.dto.GenreDTO;
import org.mapstruct.Mapper;

@Mapper
public interface GenreMapper {
    Genre toGenre(GenreDTO genreDTO);

    GenreDTO toGenreDTO(Genre genre);
}
