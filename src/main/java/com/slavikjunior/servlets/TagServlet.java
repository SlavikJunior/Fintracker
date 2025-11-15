package com.slavikjunior.servlets;

import com.slavikjunior.services.TagService;
import com.slavikjunior.util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/tags")
public class TagServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(TagServlet.class);
    private TagService tagService = new TagService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String transactionIdStr = req.getParameter("transactionId");
        String transactionType = req.getParameter("transactionType");
        String[] tagIds = req.getParameterValues("tagIds");

        if (transactionIdStr == null || transactionType == null) {
            resp.sendRedirect(req.getContextPath() + "/main?error=invalid_data");
            return;
        }

        try {
            int transactionId = Integer.parseInt(transactionIdStr);

            // Удаляем все текущие теги транзакции
            clearTransactionTags(transactionId, transactionType);

            // Добавляем новые теги
            if (tagIds != null && tagIds.length > 0) {
                for (String tagIdStr : tagIds) {
                    int tagId = Integer.parseInt(tagIdStr);
                    tagService.addTagToTransaction(transactionId, transactionType, tagId);
                }
                log.info("✅ Added " + tagIds.length + " tags to transaction " + transactionId);
            }

            resp.sendRedirect(req.getContextPath() + "/main?success=tags_updated");

        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/main?error=invalid_id");
        } catch (Exception e) {
            log.severe("💥 Error updating transaction tags: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/main?error=tag_update_failed");
        }
    }

    private void clearTransactionTags(int transactionId, String transactionType) {
        try {
            // Получаем все текущие теги транзакции
            var currentTags = tagService.getTagsForTransaction(transactionId, transactionType);
            for (var tag : currentTags) {
                tagService.removeTagFromTransaction(transactionId, transactionType, tag.getId());
            }
        } catch (Exception e) {
            log.warning("⚠️ Error clearing transaction tags: " + e.getMessage());
        }
    }
}