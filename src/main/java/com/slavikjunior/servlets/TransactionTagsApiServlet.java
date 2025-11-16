package com.slavikjunior.servlets;

import com.slavikjunior.models.Tag;
import com.slavikjunior.services.TagService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/transaction-tags")
public class TransactionTagsApiServlet extends HttpServlet {
    private final TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String tid = req.getParameter("transactionId");
        if (tid == null) {
            resp.setStatus(400);
            return;
        }

        int transactionId = Integer.parseInt(tid);
        List<Integer> tagIds = tagService.getTagsForTransaction(transactionId)
                .stream()
                .map(Tag::getId)
                .toList();

        resp.setContentType("application/json");
        resp.getWriter().print(tagIds);
    }
}