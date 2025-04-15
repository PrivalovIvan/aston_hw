package com.aston.personal_book_library.service;

import com.aston.personal_book_library.domain.service.PasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncoderImpl implements PasswordEncoder {
    @Override
    public String encode(String password) {
        if (password == null || password.isEmpty()) {
            new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.checkpw(password, encodedPassword);
    }
}
