package com.k0s.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
    private long id;
    private  String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;
}
