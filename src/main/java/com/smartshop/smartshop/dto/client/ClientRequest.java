package com.smartshop.smartshop.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {
    private String companyName;
    private String contactName;
    private String email;
    private String phone;
}

