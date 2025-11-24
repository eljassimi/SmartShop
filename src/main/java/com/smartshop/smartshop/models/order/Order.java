package com.smartshop.smartshop.models.order;

import com.smartshop.smartshop.models.client.Client;
import com.smartshop.smartshop.models.payment.Payment;
import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true, length = 100)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "subtotal_amount", nullable = false)
    private double subtotalAmount;

    @Column(name = "discount_amount", nullable = false)
    private double discountAmount;

    @Column(name = "net_amount", nullable = false)
    private double netAmount;

    @Column(name = "vat_rate", nullable = false)
    private double vatRate;

    @Column(name = "vat_amount", nullable = false)
    private double vatAmount;

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @Column(name = "remaining_amount", nullable = false)
    private double remainingAmount;

    @Column(name = "promo_code", length = 50)
    private String promoCode;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();
}

