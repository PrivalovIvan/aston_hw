package com.aston.personal_book_library.datasource;

import java.sql.Connection;
import java.util.Properties;

public interface DataSource {
    void initDB(Properties properties);

    Connection getConnection();
}
