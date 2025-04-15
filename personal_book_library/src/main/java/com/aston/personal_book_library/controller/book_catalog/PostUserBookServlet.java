package com.aston.personal_book_library.controller.book_catalog;

import com.aston.personal_book_library.config.AppConfig;
import com.aston.personal_book_library.domain.service.UserBookService;
import com.aston.personal_book_library.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.UUID;

import static com.aston.personal_book_library.common.response.SendResponse.sendResponse;

@WebServlet("/book-catalog/*")
public class PostUserBookServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UserBookService userBookService;

    //region Constructor
    public PostUserBookServlet() {
        this(
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getUserBookService()
        );
    }

    public PostUserBookServlet(ObjectMapper objectMapper, UserBookService userBookService) {
        this.objectMapper = objectMapper;
        this.userBookService = userBookService;
    }
    //endregion

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        UUID userId = user.getId();
        switch (pathInfo) {
            case "/add" -> {
                String bookId = req.getParameter("book_id");
                if (userBookService.addBookToUser(userId, UUID.fromString(bookId))) {
                    sendResponse(resp, "Book added to user successfully",
                            HttpServletResponse.SC_OK);
                } else {
                    sendResponse(resp, "Book already exists in user's library",
                            HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        HttpSession session = req.getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        UUID userId = user.getId();
        switch (pathInfo) {
            case "/delete" -> {
                String bookId = req.getParameter("book_id");
                userBookService.removeBookFromUser(userId, UUID.fromString(bookId));
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }
    }
}
