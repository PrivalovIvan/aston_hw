package com.aston.personal_book_library.mapper;

import com.aston.personal_book_library.domain.model.User;
import com.aston.personal_book_library.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
    User toUser(UserDTO user);

    @Mapping(target = "password", ignore = true)
    UserDTO toUserDTO(User user);
}
