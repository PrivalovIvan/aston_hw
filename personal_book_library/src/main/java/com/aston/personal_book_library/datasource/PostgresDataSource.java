package com.aston.personal_book_library.datasource;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class PostgresDataSource implements DataSource {
    static {
        try {
            log.info("Loading PostgreSQL JDBC Driver...");
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("PostgreSQL JDBC Driver not found", e);
            throw new RuntimeException("PostgreSQL JDBC Driver not found", e);
        }
    }

    private String url;
    private String username;
    private String password;

    public void initDB(Properties properties) {
        log.info("Initializing database connection...");
        url = properties.getProperty("db.url");
        username = properties.getProperty("db.username");
        password = properties.getProperty("db.password");

        if (url == null || username == null || password == null) {
            log.error("Database connection properties are not set");
            throw new IllegalArgumentException("You need to specify both url and username and password");
        }
    }

    public Connection getConnection() {
        try {
            log.info("Connecting to database...");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.error("Failed to connect to database", e);
        }
        return null;
    }
}
