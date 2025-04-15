package com.aston.personal_book_library.mapper;

import com.aston.personal_book_library.domain.model.Author;
import com.aston.personal_book_library.dto.AuthorDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AuthorMapper {
    Author toAuthor(AuthorDTO authorDTO);

    AuthorDTO toAuthorDTO(Author author);
}
