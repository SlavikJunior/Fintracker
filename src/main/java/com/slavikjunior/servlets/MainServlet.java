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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/main")
public class MainServlet extends HttpServlet {

    private static final Logger log = AppLogger.get(MainServlet.class);
    private TransactionService transactionService = new TransactionService();
    private TagService tagService = new TagService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.info("GET /main");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.USER_ID) == null) {
            log.warning("❌ MainServlet: No user session found");
            response.sendRedirect(request.getContextPath() + "/auth");
            return;
        }

        int userId = (int) session.getAttribute(SessionConstants.USER_ID);
        log.info("✅ MainServlet: User ID = " + userId);

        try {
            // Получаем параметры фильтрации
            String filterType = request.getParameter("type");
            String filterCategory = request.getParameter("category");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String filterTag = request.getParameter("tag"); // Новый параметр фильтра по тегу

            LocalDate startDate = null;
            LocalDate endDate = null;

            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDate.parse(startDateStr);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDate.parse(endDateStr);
            }

            // Получаем транзакции с учетом фильтров
            List<TransactionItem> transactions = transactionService.getUserTransactionsWithFilters(
                    userId, filterType, filterCategory, startDate, endDate
            );

            // Применяем фильтр по тегу если указан
            if (filterTag != null && !filterTag.isEmpty()) {
                transactions = transactions.stream()
                        .filter(t -> t.getTagNames().contains(filterTag))
                        .collect(Collectors.toList());
            }

            int count = transactions.size();
            log.info("📊 Loaded " + count + " transactions with filters");

            // Группируем транзакции по дням
            Map<java.sql.Date, List<TransactionItem>> groupedTransactions = transactions.stream()
                    .collect(Collectors.groupingBy(
                            t -> new java.sql.Date(t.getCreatedAt().getTime())
                    ));

            // Сортируем по дате (новые сверху)
            List<TransactionGroup> transactionGroups = groupedTransactions.entrySet().stream()
                    .sorted(Map.Entry.<java.sql.Date, List<TransactionItem>>comparingByKey().reversed())
                    .map(entry -> new TransactionGroup(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());

            // Считаем общие итоги
            BigDecimal totalIncome = transactions.stream()
                    .filter(t -> "INCOME".equals(t.getType()))
                    .map(TransactionItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalExpense = transactions.stream()
                    .filter(t -> "EXPENSE".equals(t.getType()))
                    .map(TransactionItem::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalBalance = totalIncome.subtract(totalExpense);

            // Получаем категории для фильтров
            List<String> incomeCategories = transactionService.getIncomeCategories();
            List<String> expenseCategories = transactionService.getExpenseCategories();

            // Объединяем все категории для фильтра
            List<String> allCategories = new ArrayList<>();
            allCategories.addAll(incomeCategories);
            allCategories.addAll(expenseCategories);

            // Получаем теги пользователя
            List<Tag> userTags = tagService.getUserTags(userId);
            Map<Integer, String> userTagsMap = tagService.getUserTagsMap(userId);
            List<String> userTagNames = userTags.stream()
                    .map(Tag::getName)
                    .collect(Collectors.toList());

            request.setAttribute("transactionGroups", transactionGroups);
            request.setAttribute("totalIncome", totalIncome);
            request.setAttribute("totalExpense", totalExpense);
            request.setAttribute("totalBalance", totalBalance);
            request.setAttribute("incomeCategories", incomeCategories);
            request.setAttribute("expenseCategories", expenseCategories);
            request.setAttribute("allCategories", allCategories);
            request.setAttribute("userTags", userTags);
            request.setAttribute("userTagsMap", userTagsMap);
            request.setAttribute("userTagNames", userTagNames);

            // Сохраняем параметры фильтров для отображения в форме
            request.setAttribute("filterType", filterType);
            request.setAttribute("filterCategory", filterCategory);
            request.setAttribute("filterTag", filterTag);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", new Locale("ru"));

            request.setAttribute("dateFormat", dateFormat);
            request.setAttribute("dayFormat", dayFormat);
            request.setAttribute("monthFormat", monthFormat);

            request.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(request, response);

        } catch (Exception e) {
            log.severe("💥 MainServlet: Error loading transactions - " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
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

    private void createTag(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {
        String tagName = request.getParameter("tagName");
        String tagColor = request.getParameter("tagColor");

        log.info("🔧 Creating tag: " + tagName + " for user " + userId + " with color " + tagColor);

        if (tagName == null || tagName.trim().isEmpty()) {
            log.warning("⚠️ Invalid tag name: " + tagName);
            response.sendRedirect(request.getContextPath() + "/main?error=invalid_tag_name");
            return;
        }

        try {
            Tag tag = tagService.createTag(tagName.trim(), userId, tagColor);
            if (tag != null) {
                log.info("✅ Tag created successfully: " + tag.getName() + " with ID: " + tag.getId());
                response.sendRedirect(request.getContextPath() + "/main?success=tag_created");
            } else {
                log.warning("⚠️ Tag creation failed - likely already exists: " + tagName);
                response.sendRedirect(request.getContextPath() + "/main?error=tag_already_exists");
            }
        } catch (Exception e) {
            log.severe("💥 Error creating tag: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/main?error=tag_creation_failed");
        }
    }

    private void deleteTag(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {
        String tagIdStr = request.getParameter("tagId");

        if (tagIdStr == null || tagIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/main?error=invalid_tag_id");
            return;
        }

        try {
            int tagId = Integer.parseInt(tagIdStr);
            boolean success = tagService.deleteTag(tagId);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/main?success=tag_deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/main?error=tag_deletion_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/main?error=invalid_tag_id");
        }
    }
}