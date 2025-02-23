package com.ewch.testing.springboot.app.models;

import java.util.Objects;

public class Bank {
    private Long id;
    private String name;
    private int totalTransactions;

    public Bank() {
    }

    public Bank(Long id, String name, int totalTransactions) {
        this.id = id;
        this.name = name;
        this.totalTransactions = totalTransactions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bank bank)) return false;
        return totalTransactions == bank.totalTransactions && Objects.equals(id, bank.id) && Objects.equals(name, bank.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, totalTransactions);
    }
}
