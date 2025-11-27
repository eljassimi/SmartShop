package com.smartshop.smartshop.services;

import com.smartshop.smartshop.exceptions.BusinessException;
import com.smartshop.smartshop.models.client.Client;
import com.smartshop.smartshop.models.client.CustomerTier;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LoyaltyService {

    private static final Pattern PROMO_PATTERN = Pattern.compile("PROMO-[A-Z0-9]{4}");

    private final double vatRatePercent;

    public LoyaltyService(@Value("20") double vatRatePercent) {
        this.vatRatePercent = vatRatePercent;
    }

    public double getVatRatePercent() {
        return vatRatePercent;
    }

    public double calculateLoyaltyDiscount(Client client, double subtotal) {
        if (client == null || client.getTier() == null) {
            return 0;
        }
        CustomerTier tier = client.getTier();
        if (tier == CustomerTier.SILVER && subtotal >= 500) {
            return subtotal * 0.05;
        }
        if (tier == CustomerTier.GOLD && subtotal >= 800) {
            return subtotal * 0.10;
        }
        if (tier == CustomerTier.PLATINUM && subtotal >= 1200) {
            return subtotal * 0.15;
        }
        return 0;
    }

    public double calculatePromoDiscount(String promoCode, double subtotal) {
        if (!StringUtils.hasText(promoCode)) {
            return 0;
        }
        if (!PROMO_PATTERN.matcher(promoCode.trim().toUpperCase()).matches()) {
            throw new BusinessException("Invalid promo code format. Expected PROMO-XXXX");
        }
        return subtotal * 0.05;
    }

    public CustomerTier determineTier(int totalOrders, double totalSpent) {
        if (totalOrders >= 20 || totalSpent >= 15000) {
            return CustomerTier.PLATINUM;
        }
        if (totalOrders >= 10 || totalSpent >= 5000) {
            return CustomerTier.GOLD;
        }
        if (totalOrders >= 3 || totalSpent >= 1000) {
            return CustomerTier.SILVER;
        }
        return CustomerTier.BASIC;
    }

    public CustomerTier refreshTierForClient(Client client) {
        CustomerTier newTier = determineTier(
                client.getTotalOrders() == null ? 0 : client.getTotalOrders(),
                client.getTotalSpent());
        client.setTier(newTier);
        return newTier;
    }
}

