package com.smartshop.smartshop.dto.payment;

import com.smartshop.smartshop.models.payment.PaymentMethod;
import com.smartshop.smartshop.models.payment.PaymentStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Integer paymentNumber;
    private double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private LocalDate paymentDate;
    private LocalDate settlementDate;
    private String reference;
    private String bankName;
    private String notes;
    private OffsetDateTime createdAt;
}

