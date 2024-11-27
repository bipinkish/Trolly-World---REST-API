package com.trollyworld.bootapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prdId;
    @NotEmpty(message = "Product name cannot be empty")
    @Size(min = 3,message = "Product Name should be atleast 3 characters!")
    private String prdName;
    private String image;
    @Size(min = 6,message = "Product Description should be atleast 6 characters!")
    private String prdDesc;
    private Integer quantity;
    private double actlPrice;
    private double discount;
    private double splPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;
}
