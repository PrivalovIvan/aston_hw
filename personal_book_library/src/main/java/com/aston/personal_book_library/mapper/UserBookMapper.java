package com.aston.personal_book_library.mapper;

import com.aston.personal_book_library.domain.model.UserBook;
import com.aston.personal_book_library.dto.UserBookDTO;
import org.mapstruct.Mapper;

@Mapper
public interface UserBookMapper {
    UserBook toUserBook(UserBookDTO userBookDTO);

    UserBookDTO toUserBookDTO(UserBook userBook);
}
