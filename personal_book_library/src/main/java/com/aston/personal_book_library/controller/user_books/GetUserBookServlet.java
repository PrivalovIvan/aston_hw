package com.aston.personal_book_library.controller.user_books;

import com.aston.personal_book_library.config.AppConfig;
import com.aston.personal_book_library.domain.service.UserBookService;
import com.aston.personal_book_library.dto.UserBookDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/my-catalog/*")
public class GetUserBookServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UserBookService userBookService;

    //region constructors
    public GetUserBookServlet() {
        this(
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getUserBookService());
    }

    public GetUserBookServlet(ObjectMapper objectMapper, UserBookService userBookService) {
        this.objectMapper = objectMapper;
        this.userBookService = userBookService;
    }
    //endregion

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        List<UserBookDTO> userBooks = switch (pathInfo) {
            case "/getAll" -> userBookService.getBooksByUserId(UUID.fromString(req.getParameter("user_id")));
            default -> {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid endpoint");
                yield null;
            }
        };
        if (userBooks != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(userBooks));
        } else {
            resp.sendError(HttpServletResponse.SC_NO_CONTENT, "No books found");
        }
    }
}
