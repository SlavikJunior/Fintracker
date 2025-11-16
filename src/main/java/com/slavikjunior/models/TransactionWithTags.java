package com.slavikjunior.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class TransactionWithTags implements TransactionItem {
    private final int id;
    private final int userId;
    private final BigDecimal amount;
    private final String category;
    private final String description;
    private final Timestamp createdAt;
    private final String type;
    private final List<Tag> tags;

    public TransactionWithTags(int id, int userId, BigDecimal amount, String category,
                               String description, Timestamp createdAt, String type, List<Tag> tags) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.createdAt = createdAt;
        this.type = type;
        this.tags = tags;
    }

    @Override
    public int getId() { return id; }
    @Override
    public int getUserId() { return userId; }
    @Override
    public BigDecimal getAmount() { return amount; }
    @Override
    public String getCategory() { return category; }
    @Override
    public String getDescription() { return description; }
    @Override
    public Timestamp getCreatedAt() { return createdAt; }
    @Override
    public String getType() { return type; }
    @Override
    public List<String> getTagNames() {
        return tags.stream().map(Tag::getName).toList();
    }

    public List<Tag> getTags() { return tags; }
}