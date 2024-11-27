package com.trollyworld.bootapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long prdId;
    private String prdName;
    private String image;
    private String prdDesc;
    private Integer quantity;
    private double actlPrice;
    private double discount;
    private double splPrice;
}
