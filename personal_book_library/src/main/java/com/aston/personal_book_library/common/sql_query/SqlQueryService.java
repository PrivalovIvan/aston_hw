package com.aston.personal_book_library.common.sql_query;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface SqlQueryService {
    <T> Optional<T> executeQueryForSingleResult(
            String sql,
            ThrowingConsumer<PreparedStatement> preparer,
            ThrowingFunction<ResultSet, T> mapper
    );

    <T> List<T> executeQuery(
            String sql,
            ThrowingConsumer<PreparedStatement> parameterSetter,
            ThrowingFunction<ResultSet, T> mapper);

    <T> List<T> executeQuery(
            String sql,
            ThrowingFunction<ResultSet, T> mapper);

    <T> void executeUpdate(
            String sql,
            ThrowingConsumer<PreparedStatement> parameterSetter
    );
}
