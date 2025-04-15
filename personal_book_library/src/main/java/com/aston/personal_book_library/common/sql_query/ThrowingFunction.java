package com.aston.personal_book_library.common.sql_query;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowingFunction<T, R> {
    R apply(T t) throws SQLException;
}
