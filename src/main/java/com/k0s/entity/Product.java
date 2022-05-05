package com.k0s.entity;


import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Product {
    private long id;
    private  String name;
    private double price;
    private LocalDateTime creationDate;
    private String description;

}
