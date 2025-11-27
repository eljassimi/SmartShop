package com.smartshop.smartshop.dto.client;

import com.smartshop.smartshop.models.client.CustomerTier;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {
    private Long id;
    private String companyName;
    private String contactName;
    private String email;
    private String phone;
    private CustomerTier tier;
    private Integer totalOrders;
    private double totalSpent;
    private OffsetDateTime firstOrderAt;
    private OffsetDateTime lastOrderAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

