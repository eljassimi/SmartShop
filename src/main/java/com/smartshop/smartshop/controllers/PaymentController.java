package com.smartshop.smartshop.controllers;

import com.smartshop.smartshop.dto.payment.PaymentRequest;
import com.smartshop.smartshop.dto.payment.PaymentResponse;
import com.smartshop.smartshop.models.user.UserRole;
import com.smartshop.smartshop.services.PaymentService;
import com.smartshop.smartshop.session.SessionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final SessionService sessionService;

    private void requireAdmin() {
        sessionService.requireRole(UserRole.ADMIN);
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentRequest request) {
        requireAdmin();
        return ResponseEntity.ok(paymentService.recordPayment(request));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsForOrder(@PathVariable Long orderId) {
        requireAdmin();
        return ResponseEntity.ok(paymentService.getPaymentsForOrder(orderId));
    }
}


