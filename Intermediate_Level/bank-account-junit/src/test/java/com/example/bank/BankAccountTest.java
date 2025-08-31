package com.example.bank;

import com.example.bank.exceptions.InsufficientFundsException;
import com.example.bank.exceptions.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void deposit_shouldIncreaseBalance_andCreateTransaction() {
        BankAccount acc = new BankAccount("A1", "Alice");
        acc.deposit(new BigDecimal("100.00"), "initial");
        assertEquals(new BigDecimal("100.00"), acc.getBalance());

        var txns = acc.getTransactions();
        assertEquals(1, txns.size());
        assertEquals(TransactionType.DEPOSIT, txns.get(0).getType());
        assertEquals(new BigDecimal("100.00"), txns.get(0).getAmount());
        assertNotNull(txns.get(0).getTimestamp());
        assertEquals(new BigDecimal("100.00"), txns.get(0).getBalanceAfter());
    }

    @Test
    void deposit_zeroOrNegative_shouldThrow() {
        BankAccount acc = new BankAccount("A1", "Alice");
        assertThrows(InvalidAmountException.class, () -> acc.deposit(new BigDecimal("0"), "zero"));
        assertThrows(InvalidAmountException.class, () -> acc.deposit(new BigDecimal("-10"), "negative"));
    }

    @Test
    void withdraw_shouldDecreaseBalance_andCreateTransaction() {
        BankAccount acc = new BankAccount("A1", "Alice");
        acc.deposit(new BigDecimal("150.00"), "fund");
        acc.withdraw(new BigDecimal("50.00"), "atm");
        assertEquals(new BigDecimal("100.00"), acc.getBalance());

        var txns = acc.getTransactions();
        assertEquals(2, txns.size());
        assertEquals(TransactionType.WITHDRAWAL, txns.get(1).getType());
        assertEquals(new BigDecimal("50.00"), txns.get(1).getAmount());
        assertEquals(new BigDecimal("100.00"), txns.get(1).getBalanceAfter());
    }

    @Test
    void withdraw_moreThanBalance_shouldThrow() {
        BankAccount acc = new BankAccount("A1", "Alice");
        acc.deposit(new BigDecimal("40.00"), "fund");
        assertThrows(InsufficientFundsException.class, () -> acc.withdraw(new BigDecimal("50.00"), "too much"));
    }

    @Test
    void transactions_exposedListIsImmutableCopy() {
        BankAccount acc = new BankAccount("A1", "Alice");
        acc.deposit(new BigDecimal("10.00"), "x");
        List<Transaction> history = acc.getTransactions();
        assertThrows(UnsupportedOperationException.class, () -> history.add(history.get(0)));
    }
}
