package com.aston.personal_book_library.domain.repository;

import com.aston.personal_book_library.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> getByEmail(String email);

    void update(String email, String username);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);

}
