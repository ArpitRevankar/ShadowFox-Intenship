package com.example.bank;

import com.example.bank.exceptions.InsufficientFundsException;
import com.example.bank.exceptions.InvalidAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BankAccount {
    private final String id;
    private final String ownerName;
    private BigDecimal balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public BankAccount(String id, String ownerName) {
        this.id = Objects.requireNonNull(id, "id");
        this.ownerName = Objects.requireNonNull(ownerName, "ownerName");
        this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public String getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized BigDecimal getBalance() {
        return balance;
    }

    public synchronized List<Transaction> getTransactions() {
        return Collections.unmodifiableList(new ArrayList<>(transactions));
    }

    public synchronized Transaction deposit(BigDecimal amount, String description) {
        BigDecimal normalized = normalizeAmount(amount);
        if (normalized.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        balance = balance.add(normalized);
        Transaction t = new Transaction(TransactionType.DEPOSIT, normalized, balance, LocalDateTime.now(), description);
        transactions.add(t);
        return t;
    }

    public synchronized Transaction withdraw(BigDecimal amount, String description) {
        BigDecimal normalized = normalizeAmount(amount);
        if (normalized.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (normalized.compareTo(balance) > 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        balance = balance.subtract(normalized);
        Transaction t = new Transaction(TransactionType.WITHDRAWAL, normalized, balance, LocalDateTime.now(), description);
        transactions.add(t);
        return t;
    }

    private static BigDecimal normalizeAmount(BigDecimal amount) {
        if (amount == null) throw new InvalidAmountException("Amount cannot be null");
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
