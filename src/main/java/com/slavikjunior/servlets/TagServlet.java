package com.slavikjunior.servlets;

import com.slavikjunior.services.TagService;
import com.slavikjunior.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/tags")
public class TagServlet extends HttpServlet {
    private static final Logger log = AppLogger.get(TagServlet.class);
    private final TagService tagService = new TagService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String transactionIdStr = req.getParameter("transactionId");
        String[] tagIds = req.getParameterValues("tagIds");
        HttpSession session = req.getSession();

        if (transactionIdStr == null) {
            session.setAttribute("flashError", "Неверные данные");
            resp.sendRedirect(req.getContextPath() + "/main");
            return;
        }

        try {
            int transactionId = Integer.parseInt(transactionIdStr);

            tagService.clearTransactionTags(transactionId);

            if (tagIds != null) {
                for (String tagIdStr : tagIds) {
                    int tagId = Integer.parseInt(tagIdStr);
                    tagService.addTagToTransaction(transactionId, tagId);
                }
            }

            session.setAttribute("flashSuccess", "Теги транзакции обновлены");
        } catch (Exception e) {
            log.severe("Error updating transaction tags: " + e.getMessage());
            session.setAttribute("flashError", "Ошибка при обновлении тегов");
        }

        resp.sendRedirect(req.getContextPath() + "/main");
    }
}