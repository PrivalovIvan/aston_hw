package com.aston.personal_book_library.common.sql_query;

import com.aston.personal_book_library.datasource.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class SqlQueryServiceImpl implements SqlQueryService {
    private final DataSource dataSource;

    @Override
    public <T> Optional<T> executeQueryForSingleResult(
            String sql,
            ThrowingConsumer<PreparedStatement> preparers,
            ThrowingFunction<ResultSet, T> mapper) {
        try (var connect = dataSource.getConnection()) {
            var preparedStatement = connect.prepareStatement(sql);
            preparers.accept(preparedStatement);

            try (var resultSet = preparedStatement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapper.apply(resultSet)) : Optional.empty();
            } catch (Exception e) {
                log.error("Error executing query: {}", sql, e);
                throw new IllegalArgumentException("Failed to execute query: " + sql, e);
            }
        } catch (SQLException e) {
            log.error("Error preparing statement: {}", sql, e);
        }
        return Optional.empty();
    }


    @Override
    public <T> List<T> executeQuery(
            String sql,
            ThrowingConsumer<PreparedStatement> preparers,
            ThrowingFunction<ResultSet, T> mapper) {
        List<T> list = new ArrayList<>();
        try (var conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            preparers.accept(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.apply(rs));
                }
            } catch (Exception e) {
                log.error("Error executing query: {}", sql, e);
                throw new IllegalArgumentException("Failed to execute query: " + sql, e);
            }
        } catch (SQLException e) {
            log.error("Error preparing statement: {}", sql, e);
            throw new IllegalArgumentException("Failed to execute query: " + sql, e);
        }
        return list;
    }

    @Override
    public <T> List<T> executeQuery(String sql, ThrowingFunction<ResultSet, T> mapper) {
        return executeQuery(sql, ps -> {
        }, mapper);
    }

    @Override
    public <T> void executeUpdate(String sql, ThrowingConsumer<PreparedStatement> parameterSetter) {
        try (var conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            parameterSetter.accept(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            log.error("Error preparing statement: {}", sql, e);
            throw new IllegalArgumentException("Failed to execute update: " + sql, e);
        }
    }
}