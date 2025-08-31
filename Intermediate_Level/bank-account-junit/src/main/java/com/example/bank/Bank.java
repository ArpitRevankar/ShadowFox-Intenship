package com.example.bank;

import com.example.bank.exceptions.AccountNotFoundException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private final Map<String, BankAccount> accounts = new ConcurrentHashMap<>();

    public BankAccount createAccount(String ownerName) {
        Objects.requireNonNull(ownerName, "ownerName");
        String id = UUID.randomUUID().toString();
        BankAccount account = new BankAccount(id, ownerName);
        accounts.put(id, account);
        return account;
    }

    public BankAccount getAccount(String id) {
        BankAccount acc = accounts.get(id);
        if (acc == null) {
            throw new AccountNotFoundException("No account with id " + id);
        }
        return acc;
    }

    public void deposit(String accountId, BigDecimal amount, String description) {
        getAccount(accountId).deposit(amount, description);
    }

    public void withdraw(String accountId, BigDecimal amount, String description) {
        getAccount(accountId).withdraw(amount, description);
    }
}
