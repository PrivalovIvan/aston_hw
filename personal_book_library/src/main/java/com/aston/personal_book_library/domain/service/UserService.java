package com.aston.personal_book_library.domain.service;

import com.aston.personal_book_library.dto.UserDTO;

import java.util.Optional;

public interface UserService {
    boolean register(UserDTO userDTO);

    UserDTO login(String email, String password);

    void updateUser(String email, String username);

    Optional<UserDTO> getUserByEmail(String email);

    void deleteUserByEmail(String email);
}
