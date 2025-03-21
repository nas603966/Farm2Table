package com.farm2table.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private Double price;
    private String description;
    private String imageUrl;
    private int quantityAvailable;

    @ManyToOne
    @JoinColumn(name = "farmer_id")
    private User farmer;
}