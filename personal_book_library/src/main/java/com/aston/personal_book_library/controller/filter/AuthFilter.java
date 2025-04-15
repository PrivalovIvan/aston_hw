package com.aston.personal_book_library.controller.filter;

import com.aston.personal_book_library.common.annotation.Role;
import com.aston.personal_book_library.dto.UserDTO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/profile/*", "/my-catalog/*", "/admin/*"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String pathInfo = request.getPathInfo();
        String servletPath = request.getServletPath();

        System.out.println("ServletPath: " + servletPath);
        System.out.println("AuthFilter: " + pathInfo);

        if (pathInfo.equals("/login") || pathInfo.equals("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        UserDTO loggedUser = (UserDTO) session.getAttribute("user");
        if (loggedUser.getRole().equals(Role.ADMIN)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (servletPath.startsWith("/admin")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied, only Admin");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
