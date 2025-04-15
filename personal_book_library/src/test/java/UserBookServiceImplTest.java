import com.aston.personal_book_library.domain.model.UserBook;
import com.aston.personal_book_library.domain.repository.UserBookRepository;
import com.aston.personal_book_library.dto.UserBookDTO;
import com.aston.personal_book_library.mapper.UserBookMapper;
import com.aston.personal_book_library.service.UserBookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserBookServiceImplTest {

    private final UUID userId = UUID.randomUUID();
    private final UUID bookId = UUID.randomUUID();
    @Mock
    private UserBookRepository userBookRepository;
    @Spy
    private UserBookMapper userBookMapper = Mappers.getMapper(UserBookMapper.class);
    @InjectMocks
    private UserBookServiceImpl userBookService;
    private UserBook userBook;
    private UserBookDTO userBookDTO;

    @BeforeEach
    void setUp() {
        userBook = UserBook.builder()
                .id(UUID.randomUUID())
                .build();

        userBookDTO = UserBookDTO.builder()
                .id(userBook.getId())
                .build();
    }

    @Test
    void addBookToUser_whenBookNotExists_shouldReturnTrue() {
        when(userBookRepository.addBookToUser(userId, bookId)).thenReturn(true);

        boolean result = userBookService.addBookToUser(userId, bookId);

        assertTrue(result);
        verify(userBookRepository, times(1)).addBookToUser(userId, bookId);
    }

    @Test
    void addBookToUser_whenBookExists_shouldReturnFalse() {
        when(userBookRepository.addBookToUser(userId, bookId)).thenReturn(false);

        boolean result = userBookService.addBookToUser(userId, bookId);

        assertFalse(result);
        verify(userBookRepository, times(1)).addBookToUser(userId, bookId);
    }

    @Test
    void removeBookFromUser_shouldCallRepository() {
        userBookService.removeBookFromUser(userId, bookId);

        verify(userBookRepository, times(1)).removeBookFromUser(userId, bookId);
    }

    @Test
    void getBooksByUserId_shouldReturnMappedDTOs() {
        List<UserBook> userBooks = List.of(userBook);
        when(userBookRepository.getBooksByUserId(userId)).thenReturn(userBooks);

        List<UserBookDTO> result = userBookService.getBooksByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userBook.getId(), result.get(0).getId());
        verify(userBookRepository, times(1)).getBooksByUserId(userId);
    }

    @Test
    void getBooksByUserId_whenNoBooks_shouldReturnEmptyList() {
        when(userBookRepository.getBooksByUserId(userId)).thenReturn(List.of());

        List<UserBookDTO> result = userBookService.getBooksByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userBookRepository, times(1)).getBooksByUserId(userId);
    }
}