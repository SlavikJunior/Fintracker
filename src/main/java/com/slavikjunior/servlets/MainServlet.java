package com.slavikjunior.servlets;

import com.slavikjunior.models.TransactionGroup;
import com.slavikjunior.models.TransactionItem;
import com.slavikjunior.models.Tag;
import com.slavikjunior.services.TransactionService;
import com.slavikjunior.services.TagService;
import com.slavikjunior.util.AppLogger;
import com.slavikjunior.util.SessionConstants;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private static final Logger log = AppLogger.get(MainServlet.class);
    private TransactionService transactionService = new TransactionService();
    private TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.USER_ID) == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        int userId = (int) session.getAttribute(SessionConstants.USER_ID);

        try {
            loadPageData(request, response, userId);
        } catch (Exception e) {
            log.severe("Error loading main page: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.USER_ID) == null) {
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        int userId = (int) session.getAttribute(SessionConstants.USER_ID);
        String action = request.getParameter("action");

        if ("createTag".equals(action)) {
            createTag(request, response, userId);
        } else if ("deleteTag".equals(action)) {
            deleteTag(request, response, userId);
        } else {
            response.sendRedirect(request.getContextPath() + "/main");
        }
    }

    private void loadPageData(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        Map<String, String> filters = extractFilters(request);
        List<TransactionItem> transactions = loadFilteredTransactions(userId, filters);

        setupRequestAttributes(request, userId, transactions, filters);
        request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);
    }

    private Map<String, String> extractFilters(HttpServletRequest request) {
        Map<String, String> filters = new HashMap<>();
        filters.put("type", request.getParameter("type"));
        filters.put("category", request.getParameter("category"));
        filters.put("tag", request.getParameter("tag"));
        filters.put("startDate", request.getParameter("startDate"));
        filters.put("endDate", request.getParameter("endDate"));
        return filters;
    }

    private List<TransactionItem> loadFilteredTransactions(int userId, Map<String, String> filters) {
        LocalDate startDate = parseDate(filters.get("startDate"));
        LocalDate endDate = parseDate(filters.get("endDate"));

        List<TransactionItem> transactions = transactionService.getUserTransactionsWithFilters(
                userId, filters.get("type"), filters.get("category"), startDate, endDate
        );

        if (filters.get("tag") != null && !filters.get("tag").isEmpty()) {
            transactions = transactions.stream()
                    .filter(t -> t.getTagNames().contains(filters.get("tag")))
                    .collect(Collectors.toList());
        }

        log.info("Loaded " + transactions.size() + " transactions with filters");
        return transactions;
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr != null && !dateStr.isEmpty()) {
            return LocalDate.parse(dateStr);
        }
        return null;
    }

    private void setupRequestAttributes(HttpServletRequest request, int userId, List<TransactionItem> transactions, Map<String, String> filters) {
        List<TransactionGroup> transactionGroups = groupTransactions(transactions);
        Map<String, BigDecimal> totals = calculateTotals(transactions);

        request.setAttribute("transactionGroups", transactionGroups);
        request.setAttribute("totalIncome", totals.get("income"));
        request.setAttribute("totalExpense", totals.get("expense"));
        request.setAttribute("totalBalance", totals.get("balance"));

        setupCategoriesAndTags(request, userId);
        setupDateFormats(request);
        setupFilterAttributes(request, filters);
    }

    private List<TransactionGroup> groupTransactions(List<TransactionItem> transactions) {
        Map<java.sql.Date, List<TransactionItem>> grouped = transactions.stream()
                .collect(Collectors.groupingBy(t -> new java.sql.Date(t.getCreatedAt().getTime())));

        return grouped.entrySet().stream()
                .sorted(Map.Entry.<java.sql.Date, List<TransactionItem>>comparingByKey().reversed())
                .map(entry -> new TransactionGroup(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<String, BigDecimal> calculateTotals(List<TransactionItem> transactions) {
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> "INCOME".equals(t.getType()))
                .map(TransactionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> "EXPENSE".equals(t.getType()))
                .map(TransactionItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, BigDecimal> totals = new HashMap<>();
        totals.put("income", totalIncome);
        totals.put("expense", totalExpense);
        totals.put("balance", totalIncome.subtract(totalExpense));

        return totals;
    }

    private void setupCategoriesAndTags(HttpServletRequest request, int userId) {
        request.setAttribute("incomeCategories", transactionService.getIncomeCategories());
        request.setAttribute("expenseCategories", transactionService.getExpenseCategories());

        List<String> allCategories = new ArrayList<>();
        allCategories.addAll(transactionService.getIncomeCategories());
        allCategories.addAll(transactionService.getExpenseCategories());
        request.setAttribute("allCategories", allCategories);

        List<Tag> userTags = tagService.getUserTags(userId);
        request.setAttribute("userTags", userTags);
        request.setAttribute("userTagNames", userTags.stream().map(Tag::getName).collect(Collectors.toList()));
    }

    private void setupDateFormats(HttpServletRequest request) {
        request.setAttribute("dateFormat", new SimpleDateFormat("dd.MM.yyyy HH:mm"));
        request.setAttribute("dayFormat", new SimpleDateFormat("dd.MM.yyyy"));
        request.setAttribute("monthFormat", new SimpleDateFormat("MMMM yyyy", new Locale("ru")));
    }

    private void setupFilterAttributes(HttpServletRequest request, Map<String, String> filters) {
        filters.forEach(request::setAttribute);
    }

    private void createTag(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
        String tagName = request.getParameter("tagName");
        String tagColor = request.getParameter("tagColor");

        if (tagName == null || tagName.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/main?error=invalid_tag_name");
            return;
        }

        try {
            Tag tag = tagService.createTag(tagName.trim(), userId, tagColor);
            if (tag != null) {
                response.sendRedirect(request.getContextPath() + "/main?success=tag_created");
            } else {
                response.sendRedirect(request.getContextPath() + "/main?error=tag_already_exists");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/main?error=tag_creation_failed");
        }
    }

    private void deleteTag(HttpServletRequest request, HttpServletResponse response, int userId) throws IOException {
        String tagIdStr = request.getParameter("tagId");
        if (tagIdStr == null || tagIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/main?error=invalid_tag_id");
            return;
        }

        try {
            boolean success = tagService.deleteTag(Integer.parseInt(tagIdStr));
            String redirect = success ? "?success=tag_deleted" : "?error=tag_deletion_failed";
            response.sendRedirect(request.getContextPath() + "/main" + redirect);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/main?error=invalid_tag_id");
        }
    }
}