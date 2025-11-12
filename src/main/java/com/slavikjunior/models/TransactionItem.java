package com.slavikjunior.models;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface TransactionItem {
    int getId();
    int getUserId();
    BigDecimal getAmount();
    String getCategory();
    String getDescription();
    Timestamp getCreatedAt();
    String getType();
}