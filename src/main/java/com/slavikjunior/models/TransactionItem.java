package com.slavikjunior.models;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public interface TransactionItem {
    int getId();
    int getUserId();
    BigDecimal getAmount();
    String getCategory();
    String getDescription();
    Timestamp getCreatedAt();
    String getType();
    List<String> getTagNames();
}