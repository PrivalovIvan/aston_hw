package com.aston.personal_book_library.service;

import com.aston.personal_book_library.common.annotation.Role;
import com.aston.personal_book_library.domain.repository.UserRepository;
import com.aston.personal_book_library.domain.service.PasswordEncoder;
import com.aston.personal_book_library.domain.service.UserService;
import com.aston.personal_book_library.dto.UserDTO;
import com.aston.personal_book_library.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public boolean register(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return false;
        }
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        UserDTO createdUser = UserDTO.builder()
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        userRepository.save(userMapper.toUser(createdUser));
        return true;
    }

    @Override
    public UserDTO login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password must not be null");
        }
        return userRepository.getByEmail(email).map(user -> {
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new IllegalArgumentException("Wrong password");
            }
            return userMapper.toUserDTO(user);
        }).orElse(null);
    }


    @Override
    public void updateUser(String email, String username) {
        userRepository.update(email, username);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.getByEmail(email).map(userMapper::toUserDTO);
    }

    @Override
    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
}
