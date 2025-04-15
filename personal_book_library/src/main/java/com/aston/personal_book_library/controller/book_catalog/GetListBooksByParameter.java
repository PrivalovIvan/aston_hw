package com.aston.personal_book_library.controller.book_catalog;

import com.aston.personal_book_library.config.AppConfig;
import com.aston.personal_book_library.domain.service.BookCatalogService;
import com.aston.personal_book_library.dto.BookDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.aston.personal_book_library.common.response.SendResponse.sendResponse;

@WebServlet("/book-catalog/list/*")
public class GetListBooksByParameter extends HttpServlet {
    private final BookCatalogService bookCatalogService;
    private final ObjectMapper objectMapper;

    //region constructors
    public GetListBooksByParameter() {
        this(
                AppConfig.getInstance().getBookCatalogService(),
                AppConfig.getInstance().getObjectMapper());
    }

    public GetListBooksByParameter(BookCatalogService bookCatalogService, ObjectMapper objectMapper) {
        this.bookCatalogService = bookCatalogService;
        this.objectMapper = objectMapper;
    }
    //endregion

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        List<BookDTO> books = switch (pathInfo) {
            case "/all" -> bookCatalogService.findAll();
            case "/author" -> {
                String authorId = req.getParameter("author_id");
                try {
                    yield bookCatalogService.findByAuthor(UUID.fromString(authorId));
                } catch (IllegalArgumentException e) {
                    sendResponse(resp, "Invalid author ID format", HttpServletResponse.SC_BAD_REQUEST);
                    yield Collections.emptyList();
                }
            }
            case "/genre" -> {
                String genre = req.getParameter("genre");
                yield bookCatalogService.findByGenre(genre);
            }
            case "/year" -> {
                String startYear = req.getParameter("start_year");
                String endYear = req.getParameter("end_year");
                if (startYear == null || endYear == null) {
                    sendResponse(resp, "Invalid year range", HttpServletResponse.SC_BAD_REQUEST);
                    yield Collections.emptyList();
                }
                try {
                    yield bookCatalogService.findByYearRange(Integer.parseInt(startYear), Integer.parseInt(endYear));
                } catch (NumberFormatException e) {
                    sendResponse(resp, "Invalid year format", HttpServletResponse.SC_BAD_REQUEST);
                    yield Collections.emptyList();
                }
            }
            case "/title-containing" -> {
                String keyword = req.getParameter("keyword");
                yield bookCatalogService.findByTitleContaining(keyword);
            }
            default -> {
                sendResponse(resp, "Invalid request", HttpServletResponse.SC_BAD_REQUEST);
                yield Collections.emptyList();
            }
        };
        if (!books.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(books));
        } else {
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
