package com.example.bank;

import com.example.bank.exceptions.AccountNotFoundException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @Test
    void create_and_fetch_account() {
        Bank bank = new Bank();
        BankAccount acc = bank.createAccount("Bob");
        assertNotNull(acc.getId());
        assertEquals("Bob", acc.getOwnerName());
        assertSame(acc, bank.getAccount(acc.getId()));
    }

    @Test
    void getAccount_nonExisting_shouldThrow() {
        Bank bank = new Bank();
        assertThrows(AccountNotFoundException.class, () -> bank.getAccount("nope"));
    }

    @Test
    void deposit_and_withdraw_via_bank_facade() {
        Bank bank = new Bank();
        BankAccount acc = bank.createAccount("Carol");
        bank.deposit(acc.getId(), new BigDecimal("200.00"), "salary");
        bank.withdraw(acc.getId(), new BigDecimal("80.00"), "groceries");
        assertEquals(new BigDecimal("120.00"), bank.getAccount(acc.getId()).getBalance());
        assertEquals(2, bank.getAccount(acc.getId()).getTransactions().size());
    }
}
