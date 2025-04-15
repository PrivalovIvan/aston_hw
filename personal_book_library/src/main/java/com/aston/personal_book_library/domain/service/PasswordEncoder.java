package com.aston.personal_book_library.domain.service;

public interface PasswordEncoder {
    String encode(String password);

    boolean matches(String password, String encodedPassword);
}
