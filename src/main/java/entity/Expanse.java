package entity;

import java.time.LocalDateTime;

public class Expanse {

    private static int idCounter = 0;

    private int id;
    private LocalDateTime createdAt;
    private String description;
    private int amount;

    public Expanse(String description, int amount) {
        this.id = generateId();
        this.createdAt = getLocalDateTime();
        this.description = description;
        this.amount = amount;
    }

    public Expanse(int id, LocalDateTime createdAt, String description, int amount) {
        this.id = id;
        this.createdAt = createdAt;
        this.description = description;
        this.amount = amount;
    }

    public Expanse(int id, String description, int amount, LocalDateTime createdAt) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    private LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    private int generateId() {
        return ++idCounter;
    }
}
