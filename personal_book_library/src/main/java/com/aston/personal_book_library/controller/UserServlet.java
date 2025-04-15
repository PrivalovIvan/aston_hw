package com.aston.personal_book_library.controller;

import com.aston.personal_book_library.config.AppConfig;
import com.aston.personal_book_library.domain.service.UserService;
import com.aston.personal_book_library.dto.UserDTO;
import com.aston.personal_book_library.validator.ValidatorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Writer;

import static com.aston.personal_book_library.common.response.SendResponse.sendResponse;

@WebServlet("/profile/*")
public class UserServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final ValidatorDTO validatorDTO;

    //region constructors
    public UserServlet() {
        this(
                AppConfig.getInstance().getObjectMapper(),
                AppConfig.getInstance().getUserService(),
                AppConfig.getInstance().getValidatorDTO());
    }


    public UserServlet(ObjectMapper objectMapper, UserService userService, ValidatorDTO validatorDTO) {
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.validatorDTO = validatorDTO;
    }
    //endregion

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (Writer writer = resp.getWriter()) {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if (pathInfo.equals("/register")) {
                UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
                if (!validatorDTO.isValid(userDTO, resp)) {
                    return;
                }
                boolean isRegister = userService.register(userDTO);
                if (!isRegister) {
                    sendResponse(resp, "User with this email already exists", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                sendResponse(resp,
                        "User %s successfully registered!".formatted(userDTO.getUsername()),
                        HttpServletResponse.SC_CREATED);
            } else if (pathInfo.equals("/login")) {
                HttpSession session = req.getSession(true);
                if (session.getAttribute("user") != null) {
                    sendResponse(resp, "User already logged in", HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                String email = req.getParameter("email");
                String password = req.getParameter("password");
                UserDTO userDTO = userService.login(email, password);
                if (userDTO == null) {
                    sendResponse(resp, "Wrong email or password", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                session.setAttribute("user", userDTO);
                sendResponse(resp, "User %s successfully logged in!".formatted(userDTO.getUsername()), HttpServletResponse.SC_OK);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if (pathInfo.equals("/show")) {
            try (Writer writer = resp.getWriter()) {
                HttpSession session = req.getSession();
                UserDTO userDtoSession = (UserDTO) session.getAttribute("user");
                if (userDtoSession == null) {
                    sendResponse(resp, "User not logged in", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                String email = userDtoSession.getEmail();
                UserDTO userDTO = userService.getUserByEmail(email).orElse(null);
                writer.write(objectMapper.writeValueAsString(userDTO));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (Writer writer = resp.getWriter()) {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (pathInfo.equals("/update")) {
                String username = req.getParameter("username");
                if (username == null) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                HttpSession session = req.getSession();
                UserDTO userDTO = (UserDTO) session.getAttribute("user");
                String oldUsername = userDTO.getUsername();
                userService.updateUser(userDTO.getEmail(), username);
                writer.write("""
                        {
                            "message": "User %s successfully updated!",
                            "oldUsername": "%s",
                            "newUsername": "%s"
                        }
                        """.formatted(userDTO.getUsername(), oldUsername, username));
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (pathInfo.equals("/delete")) {
            HttpSession session = req.getSession();
            UserDTO userDTO = (UserDTO) session.getAttribute("user");
            userService.deleteUserByEmail(userDTO.getEmail());
            session.setAttribute("user", null);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
