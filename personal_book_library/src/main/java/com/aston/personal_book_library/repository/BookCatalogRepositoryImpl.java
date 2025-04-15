package com.aston.personal_book_library.repository;

import com.aston.personal_book_library.common.sql_query.SqlQueryService;
import com.aston.personal_book_library.domain.model.Book;
import com.aston.personal_book_library.domain.model.Genre;
import com.aston.personal_book_library.domain.repository.BookCatalogRepository;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
public class BookCatalogRepositoryImpl implements BookCatalogRepository {
    //region SQL Queries
    private final static String SELECT_ALL_SQL =
            "SELECT * FROM personal_book_library.catalog_books b " +
                    "LEFT JOIN personal_book_library.books_genres bg ON b.id = bg.book_id " +
                    "LEFT JOIN personal_book_library.genres g ON g.id = bg.genre_id";
    private final static String SELECT_BY_ID_SQL = SELECT_ALL_SQL + " WHERE b.id = ?";
    private final static String SELECT_BY_TITLE_SQL = SELECT_ALL_SQL + " WHERE b.title = ?";
    private final static String SELECT_BY_AUTHOR_SQL = SELECT_ALL_SQL + " WHERE b.author_id = ?";
    private final static String SELECT_BY_GENRE_SQL = SELECT_ALL_SQL + " WHERE g.name = ?";
    private final static String SELECT_BY_YEAR_RANGE_SQL = SELECT_ALL_SQL + " WHERE publication_year BETWEEN ? AND ?";
    private final static String SELECT_BY_TITLE_CONTAINING_SQL = SELECT_ALL_SQL + " WHERE b.title ILIKE ?";
    private final SqlQueryService query;
    //endregion

    @Override
    public List<Book> getAll() {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_ALL_SQL, rs -> resultSetToListBook(rs, bookMap));
        return new ArrayList<>(bookMap.values());
    }

    @Override
    public Optional<Book> getById(UUID id) {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_BY_ID_SQL,
                ps -> ps.setObject(1, id),
                rs -> resultSetToListBook(rs, bookMap));
        return Optional.ofNullable(bookMap.get(id));
    }

    @Override
    public Optional<Book> getByTitle(String title) {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_BY_TITLE_SQL,
                ps -> ps.setString(1, title),
                rs -> resultSetToListBook(rs, bookMap));
        return Optional.ofNullable(bookMap.values().stream().findFirst().orElse(null));
    }

    @Override
    public List<Book> getByAuthor(UUID authorId) {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_BY_AUTHOR_SQL,
                ps -> ps.setObject(1, authorId),
                rs -> resultSetToListBook(rs, bookMap));
        return new ArrayList<>(bookMap.values());
    }

    @Override
    public List<Book> getByGenre(String genre) {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_BY_GENRE_SQL, ps -> ps.setString(1, genre),
                rs -> resultSetToListBook(rs, bookMap));
        return new ArrayList<>(bookMap.values());
    }

    @Override
    public List<Book> getByYearRange(int startYear, int endYear) {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_BY_YEAR_RANGE_SQL, ps -> {
            ps.setInt(1, startYear);
            ps.setInt(2, endYear);
        }, rs -> resultSetToListBook(rs, bookMap));
        return new ArrayList<>(bookMap.values());
    }

    @Override
    public List<Book> getByTitleContaining(String keyword) {
        Map<UUID, Book> bookMap = new HashMap<>();
        query.executeQuery(SELECT_BY_TITLE_CONTAINING_SQL,
                ps -> ps.setString(1, "%" + keyword + "%"),
                rs -> resultSetToListBook(rs, bookMap));
        return new ArrayList<>(bookMap.values());
    }

    private Book resultSetToBook(ResultSet resultSet) {
        try {
            return Book.builder()
                    .id(resultSet.getObject("id", UUID.class))
                    .title(resultSet.getString("title"))
                    .publicationYear(resultSet.getInt("publication_year"))
                    .authorId(resultSet.getObject("author_id", UUID.class))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Book> resultSetToListBook(ResultSet resultSet, Map<UUID, Book> bookMap) {
        try {
            UUID bookId = resultSet.getObject("id", UUID.class);
            Book book = bookMap.computeIfAbsent(bookId, id -> resultSetToBook(resultSet));
            UUID genreId = resultSet.getObject("genre_id", UUID.class);
            if (genreId != null) {
                book.getGenres().add(new Genre(
                        genreId,
                        resultSet.getString("name")
                ));
            }
            return new ArrayList<>(bookMap.values());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}