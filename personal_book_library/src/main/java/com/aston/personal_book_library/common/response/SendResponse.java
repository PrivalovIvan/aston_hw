package com.aston.personal_book_library.common.response;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Writer;

public class SendResponse {
    public static void sendResponse(HttpServletResponse resp, String message, int status) throws IOException {
        resp.setStatus(status);
        try (Writer writer = resp.getWriter()) {
            writer.write("""
                    {
                        "message": "%s"
                    }
                    """.formatted(message));
        }
    }
}
