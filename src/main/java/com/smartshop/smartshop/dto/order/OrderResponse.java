package com.smartshop.smartshop.dto.order;

import com.smartshop.smartshop.dto.payment.PaymentResponse;
import com.smartshop.smartshop.models.order.OrderStatus;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private Long clientId;
    private String clientCompanyName;
    private OrderStatus status;
    private double subtotalAmount;
    private double discountAmount;
    private double netAmount;
    private double vatRate;
    private double vatAmount;
    private double totalAmount;
    private double remainingAmount;
    private String promoCode;
    private String notes;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<OrderItemResponse> items;
    private List<PaymentResponse> payments;
}

