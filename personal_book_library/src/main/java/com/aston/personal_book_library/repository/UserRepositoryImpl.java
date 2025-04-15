package com.aston.personal_book_library.repository;

import com.aston.personal_book_library.common.annotation.Role;
import com.aston.personal_book_library.common.sql_query.SqlQueryService;
import com.aston.personal_book_library.domain.model.User;
import com.aston.personal_book_library.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final static String INSERT_USER_SQL =
            "INSERT INTO personal_book_library.users (username, email, password,role) VALUES (?, ?, ?, ?)";
    private final static String SELECT_USER_BY_EMAIL_SQL = "SELECT * FROM personal_book_library.users WHERE email = ?";
    private final static String UPDATE_USER_SQL = "UPDATE personal_book_library.users SET username = ? WHERE email = ?";
    private final static String DELETE_USER_SQL = "DELETE FROM personal_book_library.users WHERE email = ?";
    private final SqlQueryService sqlQueryService;

    @Override
    public void save(User user) {
        sqlQueryService.executeUpdate(INSERT_USER_SQL, ps -> {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole().name());
        });
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return sqlQueryService.executeQueryForSingleResult(
                SELECT_USER_BY_EMAIL_SQL,
                ps -> ps.setString(1, email),
                this::resultSetToUser
        );
    }

    @Override
    public void update(String email, String username) {
        sqlQueryService.executeUpdate(UPDATE_USER_SQL, ps -> {
            ps.setString(1, username);
            ps.setString(2, email);
        });
    }

    @Override
    public void deleteByEmail(String email) {
        sqlQueryService.executeUpdate(DELETE_USER_SQL, ps -> ps.setString(1, email));
    }

    @Override
    public boolean existsByEmail(String email) {
        User user = getByEmail(email).orElse(null);
        return user != null;
    }

    private User resultSetToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getObject(1, UUID.class))
                .username(resultSet.getString(2))
                .email(resultSet.getString(3))
                .password(resultSet.getString(4))
                .role(Role.valueOf(resultSet.getString(5)))
                .build();
    }
}
