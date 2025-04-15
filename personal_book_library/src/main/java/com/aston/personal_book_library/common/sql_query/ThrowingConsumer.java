package com.aston.personal_book_library.common.sql_query;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T t) throws SQLException;
}