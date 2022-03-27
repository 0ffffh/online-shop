package com.k0s.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Product {
    private long id;
    private  String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;


    public Product(String name, double price, String description, LocalDateTime creationDate) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.creationDate = creationDate;
    }

    public String getCreationDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
