package com.smartshop.smartshop.dto.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String sku;
    private String name;
    private String description;
    private double unitPrice;
    private Integer stockQuantity;
}

