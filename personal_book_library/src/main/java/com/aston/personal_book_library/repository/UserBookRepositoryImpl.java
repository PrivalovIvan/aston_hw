package com.aston.personal_book_library.repository;

import com.aston.personal_book_library.common.annotation.ReadStatus;
import com.aston.personal_book_library.common.sql_query.SqlQueryService;
import com.aston.personal_book_library.domain.model.Author;
import com.aston.personal_book_library.domain.model.Book;
import com.aston.personal_book_library.domain.model.Genre;
import com.aston.personal_book_library.domain.model.UserBook;
import com.aston.personal_book_library.domain.repository.UserBookRepository;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
public class UserBookRepositoryImpl implements UserBookRepository {
    private final SqlQueryService sqlQueryService;

    //region SQL Queries
    private final String INSERT_USER_BOOK_SQL = "INSERT INTO personal_book_library.user_books (user_id, book_id, read_status) VALUES (?, ?, ?)";
    private final String CHECK_BOOK_ASSIGNED_SQL = "SELECT COUNT(*) FROM personal_book_library.user_books WHERE user_id = ? AND book_id = ?";
    private final String DELETE_USER_BOOK_SQL = "DELETE FROM personal_book_library.user_books WHERE user_id = ? AND book_id = ?";
    private final String SELECT_BOOKS_BY_USER_ID_SQL =
            "SELECT ub.id AS user_book_id, ub.read_status, cb.id AS book_id, cb.title, cb.publication_year, " +
                    "cb.author_id, a.id AS author_id, a.name AS author_name, a.country, g.id AS genre_id, g.name AS genre_name " +
                    "FROM personal_book_library.user_books ub " +
                    "JOIN personal_book_library.catalog_books cb ON ub.book_id = cb.id " +
                    "JOIN personal_book_library.authors a ON cb.author_id = a.id " +
                    "JOIN personal_book_library.books_genres bg ON cb.id = bg.book_id " +
                    "JOIN personal_book_library.genres g ON g.id = bg.genre_id " +
                    "WHERE user_id = ?";

    //endregion

    @Override
    public boolean addBookToUser(UUID userId, UUID bookId) {
        Optional<Integer> countBooks = sqlQueryService.executeQueryForSingleResult(CHECK_BOOK_ASSIGNED_SQL, ps -> {
            ps.setObject(1, userId);
            ps.setObject(2, bookId);
        }, rs -> rs.getInt(1));

        if (countBooks.isPresent() && countBooks.get() > 0) {
            return false;
        }

        sqlQueryService.executeUpdate(INSERT_USER_BOOK_SQL, ps -> {
            ps.setObject(1, userId);
            ps.setObject(2, bookId);
            ps.setString(3, ReadStatus.NOT_READ.name());
        });
        return true;
    }

    @Override
    public void removeBookFromUser(UUID userId, UUID bookId) {
        sqlQueryService.executeUpdate(DELETE_USER_BOOK_SQL, ps -> {
            ps.setObject(1, userId);
            ps.setObject(2, bookId);
        });
    }

    @Override
    public List<UserBook> getBooksByUserId(UUID userId) {
        Map<UUID, UserBook> bookMap = new HashMap<>();
        sqlQueryService.executeQuery(SELECT_BOOKS_BY_USER_ID_SQL, ps -> ps.setObject(1, userId), rs -> {
            UUID userBookId = rs.getObject("user_book_id", UUID.class);
            UserBook userBook = bookMap.computeIfAbsent(userBookId, k -> mapToUserBook(rs));
            UUID genreId = rs.getObject("genre_id", UUID.class);
            if (genreId != null) {
                userBook.getBook().getGenres().add(new Genre(genreId, rs.getString("genre_name")));
            }
            return null;
        });
        return new ArrayList<>(bookMap.values());
    }

    private UserBook mapToUserBook(ResultSet rs) {
        try {
            return UserBook.builder()
                    .id(rs.getObject("user_book_id", UUID.class))
                    .author(mapToAuthor(rs))
                    .book(mapToBook(rs))
                    .readStatus(ReadStatus.valueOf(rs.getString("read_status")))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Book mapToBook(ResultSet rs) {
        try {
            return Book.builder()
                    .id(rs.getObject("book_id", UUID.class))
                    .title(rs.getString("title"))
                    .publicationYear(rs.getInt("publication_year"))
                    .authorId(rs.getObject("author_id", UUID.class))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Author mapToAuthor(ResultSet rs) {
        try {
            return Author.builder()
                    .id(rs.getObject("author_id", UUID.class))
                    .name(rs.getString("author_name"))
                    .country(rs.getString("country"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
