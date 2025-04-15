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
import java.util.Optional;
import java.util.UUID;

import static com.aston.personal_book_library.common.response.SendResponse.sendResponse;

@WebServlet("/book-catalog/item/*")
public class GetBookByParameter extends HttpServlet {
    private final BookCatalogService bookCatalogService;
    private final ObjectMapper objectMapper;

    //region constructors
    public GetBookByParameter() {
        this(
                AppConfig.getInstance().getBookCatalogService(),
                AppConfig.getInstance().getObjectMapper());
    }

    public GetBookByParameter(BookCatalogService bookCatalogService, ObjectMapper objectMapper) {
        this.bookCatalogService = bookCatalogService;
        this.objectMapper = objectMapper;
    }
    //endregion

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        Optional<BookDTO> book = switch (pathInfo) {
            case "/id" -> {
                String id = req.getParameter("id");
                if (id == null) {
                    sendResponse(resp, "ID parameter is missing", HttpServletResponse.SC_BAD_REQUEST);
                    yield Optional.empty();
                }
                yield bookCatalogService.findById(UUID.fromString(id));
            }
            case "/title" -> {
                String title = req.getParameter("title");
                if (title == null) {
                    sendResponse(resp, "Title parameter is missing", HttpServletResponse.SC_BAD_REQUEST);
                    yield Optional.empty();
                }
                yield bookCatalogService.findByTitle(title);
            }
            default -> Optional.empty();
        };
        if (book.isPresent()) {
            resp.getWriter().write(objectMapper.writeValueAsString(book.get()));
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            sendResponse(resp, "Book not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
