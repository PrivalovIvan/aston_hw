import com.aston.personal_book_library.domain.model.Book;
import com.aston.personal_book_library.domain.model.Genre;
import com.aston.personal_book_library.domain.repository.BookCatalogRepository;
import com.aston.personal_book_library.dto.BookDTO;
import com.aston.personal_book_library.dto.GenreDTO;
import com.aston.personal_book_library.mapper.BookMapper;
import com.aston.personal_book_library.service.BookCatalogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCatalogServiceImplTest {

    @Mock
    private BookCatalogRepository bookCatalogRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookCatalogServiceImpl bookCatalogService;

    private UUID bookId;
    private UUID authorId;
    private Book book;
    private BookDTO bookDTO;
    private Genre genre;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        authorId = UUID.randomUUID();
        genre = new Genre(UUID.randomUUID(), "Fantasy");
        book = Book.builder()
                .id(bookId)
                .title("Test Book")
                .publicationYear(2020)
                .authorId(authorId)
                .build();
        book.getGenres().add(genre);
        genreDTO = GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
        bookDTO = BookDTO.builder()
                .id(bookId)
                .title(book.getTitle())
                .publicationYear(book.getPublicationYear())
                .authorId(book.getAuthorId())
                .build();
    }

    @Test
    void findAll_ReturnsListOfBookDTOs() {
        List<Book> books = Collections.singletonList(book);
        when(bookCatalogRepository.getAll()).thenReturn(books);
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        List<BookDTO> result = bookCatalogService.findAll();

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain one book");
        BookDTO resultBook = result.get(0);
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getAll();
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findAll_ReturnsEmptyList_WhenNoBooks() {
        when(bookCatalogRepository.getAll()).thenReturn(Collections.emptyList());

        List<BookDTO> result = bookCatalogService.findAll();

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty");
        verify(bookCatalogRepository).getAll();
        verifyNoInteractions(bookMapper);
    }

    @Test
    void findById_ReturnsBookDTO_WhenBookExists() {
        when(bookCatalogRepository.getById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        Optional<BookDTO> result = bookCatalogService.findById(bookId);

        assertTrue(result.isPresent(), "Result should be present");
        BookDTO resultBook = result.get();
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getById(bookId);
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findById_ReturnsEmpty_WhenBookNotFound() {
        when(bookCatalogRepository.getById(bookId)).thenReturn(Optional.empty());

        Optional<BookDTO> result = bookCatalogService.findById(bookId);

        assertFalse(result.isPresent(), "Result should be empty");
        verify(bookCatalogRepository).getById(bookId);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void findByTitle_ReturnsBookDTO_WhenBookExists() {
        String title = "Test Book";
        when(bookCatalogRepository.getByTitle(title)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        Optional<BookDTO> result = bookCatalogService.findByTitle(title);

        assertTrue(result.isPresent(), "Result should be present");
        BookDTO resultBook = result.get();
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getByTitle(title);
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findByTitle_ReturnsEmpty_WhenBookNotFound() {
        String title = "Nonexistent Book";
        when(bookCatalogRepository.getByTitle(title)).thenReturn(Optional.empty());

        Optional<BookDTO> result = bookCatalogService.findByTitle(title);

        assertFalse(result.isPresent(), "Result should be empty");
        verify(bookCatalogRepository).getByTitle(title);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void findByAuthor_ReturnsListOfBookDTOs() {
        List<Book> books = Collections.singletonList(book);
        when(bookCatalogRepository.getByAuthor(authorId)).thenReturn(books);
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        List<BookDTO> result = bookCatalogService.findByAuthor(authorId);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain one book");
        BookDTO resultBook = result.get(0);
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getByAuthor(authorId);
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findByAuthor_ReturnsEmptyList_WhenNoBooks() {
        when(bookCatalogRepository.getByAuthor(authorId)).thenReturn(Collections.emptyList());

        List<BookDTO> result = bookCatalogService.findByAuthor(authorId);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty");
        verify(bookCatalogRepository).getByAuthor(authorId);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void findByGenre_ReturnsListOfBookDTOs() {
        String genreName = "Fantasy";
        List<Book> books = Collections.singletonList(book);
        when(bookCatalogRepository.getByGenre(genreName)).thenReturn(books);
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        List<BookDTO> result = bookCatalogService.findByGenre(genreName);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain one book");
        BookDTO resultBook = result.get(0);
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getByGenre(genreName);
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findByGenre_ReturnsEmptyList_WhenNoBooks() {
        String genreName = "Nonexistent";
        when(bookCatalogRepository.getByGenre(genreName)).thenReturn(Collections.emptyList());

        List<BookDTO> result = bookCatalogService.findByGenre(genreName);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty");
        verify(bookCatalogRepository).getByGenre(genreName);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void findByYearRange_ReturnsListOfBookDTOs() {
        int startYear = 2019;
        int endYear = 2021;
        List<Book> books = Collections.singletonList(book);
        when(bookCatalogRepository.getByYearRange(startYear, endYear)).thenReturn(books);
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        List<BookDTO> result = bookCatalogService.findByYearRange(startYear, endYear);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain one book");
        BookDTO resultBook = result.get(0);
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getByYearRange(startYear, endYear);
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findByYearRange_ReturnsEmptyList_WhenNoBooks() {
        int startYear = 2019;
        int endYear = 2021;
        when(bookCatalogRepository.getByYearRange(startYear, endYear)).thenReturn(Collections.emptyList());

        List<BookDTO> result = bookCatalogService.findByYearRange(startYear, endYear);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty");
        verify(bookCatalogRepository).getByYearRange(startYear, endYear);
        verifyNoInteractions(bookMapper);
    }

    @Test
    void findByTitleContaining_ReturnsListOfBookDTOs() {
        String keyword = "Test";
        List<Book> books = Collections.singletonList(book);
        when(bookCatalogRepository.getByTitleContaining(keyword)).thenReturn(books);
        when(bookMapper.toBookDTO(any(Book.class))).thenReturn(bookDTO);
        when(bookMapper.toGenreDTOSet(anySet())).thenReturn(Collections.singleton(genreDTO));

        List<BookDTO> result = bookCatalogService.findByTitleContaining(keyword);

        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Result should contain one book");
        BookDTO resultBook = result.get(0);
        assertEquals(bookDTO.getId(), resultBook.getId(), "BookDTO ID should match");
        assertEquals(bookDTO.getTitle(), resultBook.getTitle(), "BookDTO title should match");
        assertEquals(bookDTO.getPublicationYear(), resultBook.getPublicationYear(), "BookDTO publication year should match");
        assertEquals(bookDTO.getAuthorId(), resultBook.getAuthorId(), "BookDTO author ID should match");
        assertEquals(1, resultBook.getGenres().size(), "BookDTO should have one genre");
        assertTrue(resultBook.getGenres().contains(genreDTO), "BookDTO should contain expected genre");
        verify(bookCatalogRepository).getByTitleContaining(keyword);
        verify(bookMapper).toBookDTO(any(Book.class));
        verify(bookMapper).toGenreDTOSet(anySet());
    }

    @Test
    void findByTitleContaining_ReturnsEmptyList_WhenNoBooks() {
        String keyword = "Nonexistent";
        when(bookCatalogRepository.getByTitleContaining(keyword)).thenReturn(Collections.emptyList());

        List<BookDTO> result = bookCatalogService.findByTitleContaining(keyword);

        assertNotNull(result, "Result should not be null");
        assertTrue(result.isEmpty(), "Result should be empty");
        verify(bookCatalogRepository).getByTitleContaining(keyword);
        verifyNoInteractions(bookMapper);
    }
}