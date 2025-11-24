package com.smartshop.smartshop.models.client;

import com.smartshop.smartshop.models.order.Order;
import com.smartshop.smartshop.models.user.User;
import jakarta.persistence.*;
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
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userAccount;

    @Column(name = "company_name", nullable = false, length = 255)
    private String companyName;

    @Column(name = "contact_name", length = 255)
    private String contactName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 50)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CustomerTier tier;

    @Column(name = "total_orders", nullable = false)
    private Integer totalOrders;

    @Column(name = "total_spent", nullable = false)
    private double totalSpent;

    @Column(name = "first_order_at")
    private OffsetDateTime firstOrderAt;

    @Column(name = "last_order_at")
    private OffsetDateTime lastOrderAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Order> orders;
}

