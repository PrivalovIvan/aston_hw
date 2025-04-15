package com.aston.personal_book_library.config;

import com.aston.personal_book_library.common.sql_query.SqlQueryService;
import com.aston.personal_book_library.common.sql_query.SqlQueryServiceImpl;
import com.aston.personal_book_library.datasource.DataSource;
import com.aston.personal_book_library.datasource.PostgresDataSource;
import com.aston.personal_book_library.domain.repository.BookCatalogRepository;
import com.aston.personal_book_library.domain.repository.UserBookRepository;
import com.aston.personal_book_library.domain.repository.UserRepository;
import com.aston.personal_book_library.domain.service.BookCatalogService;
import com.aston.personal_book_library.domain.service.PasswordEncoder;
import com.aston.personal_book_library.domain.service.UserBookService;
import com.aston.personal_book_library.domain.service.UserService;
import com.aston.personal_book_library.mapper.BookMapper;
import com.aston.personal_book_library.mapper.UserMapper;
import com.aston.personal_book_library.repository.BookCatalogRepositoryImpl;
import com.aston.personal_book_library.repository.UserBookRepositoryImpl;
import com.aston.personal_book_library.repository.UserRepositoryImpl;
import com.aston.personal_book_library.service.BookCatalogServiceImpl;
import com.aston.personal_book_library.service.PasswordEncoderImpl;
import com.aston.personal_book_library.service.UserBookServiceImpl;
import com.aston.personal_book_library.service.UserServiceImpl;
import com.aston.personal_book_library.validator.ValidInputData;
import com.aston.personal_book_library.validator.ValidatorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Getter;
import org.mapstruct.factory.Mappers;

import java.io.IOException;
import java.util.Properties;

@Getter
public class AppConfig {
    private static AppConfig instance;
    private final DataSource dataSource;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private final ValidatorDTO validatorDTO;
    private final SqlQueryService sqlQueryService;
    private final BookCatalogRepository bookCatalogRepository;
    private final BookCatalogService bookCatalogService;
    private final UserBookRepository userBookRepository;
    private final UserBookService userBookService;

    public AppConfig() throws IOException {
        Properties properties = loadProperties();
        dataSource = new PostgresDataSource();
        dataSource.initDB(properties);
        objectMapper = new ObjectMapper();
        passwordEncoder = new PasswordEncoderImpl();
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
        validatorDTO = new ValidInputData(validator, objectMapper);
        sqlQueryService = new SqlQueryServiceImpl(dataSource);

        //repositories
        bookCatalogRepository = new BookCatalogRepositoryImpl(sqlQueryService);
        userRepository = new UserRepositoryImpl(sqlQueryService);
        userBookRepository = new UserBookRepositoryImpl(sqlQueryService);

        //services
        userService = new UserServiceImpl(userRepository, passwordEncoder, Mappers.getMapper(UserMapper.class));
        bookCatalogService = new BookCatalogServiceImpl(bookCatalogRepository, Mappers.getMapper(BookMapper.class));
        userBookService = new UserBookServiceImpl(userBookRepository);
    }


    public static AppConfig getInstance() {
        if (instance == null) {
            try {
                instance = new AppConfig();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return instance;
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        var inputStream = AppConfig.class.getClassLoader().getResourceAsStream("application.properties");
        if (inputStream == null) {
            throw new IllegalStateException("Cannot find application.properties in classpath");
        }
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return properties;
    }
}
