package com.example.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Transaction {
    private final TransactionType type;
    private final BigDecimal amount;
    private final BigDecimal balanceAfter;
    private final LocalDateTime timestamp;
    private final String description;

    public Transaction(TransactionType type,
                       BigDecimal amount,
                       BigDecimal balanceAfter,
                       LocalDateTime timestamp,
                       String description) {
        this.type = Objects.requireNonNull(type, "type");
        this.amount = Objects.requireNonNull(amount, "amount");
        this.balanceAfter = Objects.requireNonNull(balanceAfter, "balanceAfter");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "type=" + type +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", timestamp=" + timestamp +
                (description != null ? ", description='" + description + '\'' : "") +
                '}';
    }
}