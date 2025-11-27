package com.smartshop.smartshop.dto.order;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long clientId;
    private String promoCode;
    private String notes;
    private List<OrderItemRequest> items;
}

