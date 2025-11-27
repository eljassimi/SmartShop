package com.smartshop.smartshop.dto.product;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private double unitPrice;
    private Integer stockQuantity;
    private boolean deleted;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

