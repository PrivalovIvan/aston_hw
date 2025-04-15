package com.aston.personal_book_library.validator;

import jakarta.servlet.http.HttpServletResponse;

@FunctionalInterface
public interface ValidatorDTO {
    <T> boolean isValid(T t, HttpServletResponse response);
}
