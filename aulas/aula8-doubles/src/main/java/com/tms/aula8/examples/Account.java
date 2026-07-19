package com.tms.aula8.examples;

public class Account {
    private final String id;
    private long balance;

    public Account(String id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public long getBalance() {
        return balance;
    }

    public void debit(long amount) {
        if (amount <= 0 || amount > balance) {
            throw new IllegalArgumentException("Invalid debit amount");
        }
        balance -= amount;
    }

    public void credit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid credit amount");
        }
        balance += amount;
    }
}
