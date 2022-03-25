package com.k0s.entity;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Product {
    private long id;
    private  String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;


    public Product() {
    }

    public Product(long id, String name, double price, LocalDateTime creationDate, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDate = creationDate;
        this.description = description;
    }

    public Product(String name, double price, String description, LocalDateTime creationDate) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreationDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Product{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append(", creationDate=").append(creationDate);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
