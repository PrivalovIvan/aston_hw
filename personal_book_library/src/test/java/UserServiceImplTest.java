import com.aston.personal_book_library.common.annotation.Role;
import com.aston.personal_book_library.domain.model.User;
import com.aston.personal_book_library.domain.repository.UserRepository;
import com.aston.personal_book_library.domain.service.PasswordEncoder;
import com.aston.personal_book_library.dto.UserDTO;
import com.aston.personal_book_library.mapper.UserMapper;
import com.aston.personal_book_library.service.PasswordEncoderImpl;
import com.aston.personal_book_library.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Spy
    private PasswordEncoder passwordEncoder = new PasswordEncoderImpl();

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private String email;
    private String password;
    private String username;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        password = "password123";
        username = "testuser";
        user = User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        userDTO = UserDTO.builder()
                .email(email)
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();
    }

    @Test
    void register_ReturnsTrue_WhenEmailNotTaken() {
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userMapper.toUser(any(UserDTO.class))).thenReturn(user);
        doNothing().when(userRepository).save(any(User.class));

        boolean result = userService.register(userDTO);

        assertTrue(result, "Registration should succeed");
        verify(userRepository).existsByEmail(email);
        verify(userMapper).toUser(any(UserDTO.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ReturnsFalse_WhenEmailTaken() {
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.register(userDTO);

        assertFalse(result, "Registration should fail if email is taken");
        verify(userRepository).existsByEmail(email);
    }

    @Test
    void login_ReturnsUserDTO_WhenCredentialsValid() {
        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.login(email, password);

        assertNotNull(result, "Result should not be null");
        assertEquals(userDTO.getEmail(), result.getEmail(), "Email should match");
        assertEquals(userDTO.getUsername(), result.getUsername(), "Username should match");
        assertEquals(userDTO.getRole(), result.getRole(), "Role should match");
        verify(userRepository).getByEmail(email);
        verify(userMapper).toUserDTO(user);
    }

    @Test
    void login_ThrowsIllegalArgumentException_WhenEmailNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.login(null, password),
                "Should throw IllegalArgumentException for null email");
        assertEquals("Email and password must not be null", exception.getMessage());
        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void login_ThrowsIllegalArgumentException_WhenPasswordNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.login(email, null),
                "Should throw IllegalArgumentException for null password");
        assertEquals("Email and password must not be null", exception.getMessage());
        verifyNoInteractions(userRepository, userMapper);
    }

    @Test
    void login_ThrowsIllegalArgumentException_WhenPasswordWrong() {
        String wrongPassword = "wrongPassword";
        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.login(email, wrongPassword),
                "Should throw IllegalArgumentException for wrong password");
        assertEquals("Wrong password", exception.getMessage());
        verify(userRepository).getByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void login_ReturnsNull_WhenUserNotFound() {
        when(userRepository.getByEmail(email)).thenReturn(Optional.empty());

        UserDTO result = userService.login(email, password);

        assertNull(result, "Result should be null when user not found");
        verify(userRepository).getByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void updateUser_UpdatesUserSuccessfully() {
        String newUsername = "newUsername";
        doNothing().when(userRepository).update(email, newUsername);

        userService.updateUser(email, newUsername);

        verify(userRepository).update(email, newUsername);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserByEmail_ReturnsUserDTO_WhenUserExists() {
        when(userRepository.getByEmail(email)).thenReturn(Optional.of(user));
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        Optional<UserDTO> result = userService.getUserByEmail(email);

        assertTrue(result.isPresent(), "Result should be present");
        UserDTO resultUser = result.get();
        assertEquals(userDTO.getEmail(), resultUser.getEmail(), "Email should match");
        assertEquals(userDTO.getUsername(), resultUser.getUsername(), "Username should match");
        assertEquals(userDTO.getRole(), resultUser.getRole(), "Role should match");
        verify(userRepository).getByEmail(email);
        verify(userMapper).toUserDTO(user);
    }

    @Test
    void getUserByEmail_ReturnsEmpty_WhenUserNotFound() {
        when(userRepository.getByEmail(email)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserByEmail(email);

        assertFalse(result.isPresent(), "Result should be empty");
        verify(userRepository).getByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUserByEmail_DeletesUserSuccessfully() {
        doNothing().when(userRepository).deleteByEmail(email);

        userService.deleteUserByEmail(email);

        verify(userRepository).deleteByEmail(email);
        verifyNoInteractions(userMapper);
    }
}