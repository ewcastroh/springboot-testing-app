package com.ewch.testing.springboot.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "banks")
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "total_transactions")
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

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalTransactions=" + totalTransactions +
                '}';
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
