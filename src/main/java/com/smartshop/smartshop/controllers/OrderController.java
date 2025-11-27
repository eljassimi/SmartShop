package com.smartshop.smartshop.controllers;

import com.smartshop.smartshop.dto.order.OrderRequest;
import com.smartshop.smartshop.dto.order.OrderResponse;
import com.smartshop.smartshop.dto.client.ClientResponse;
import com.smartshop.smartshop.models.user.UserRole;
import com.smartshop.smartshop.services.ClientService;
import com.smartshop.smartshop.services.OrderService;
import com.smartshop.smartshop.session.SessionService;
import com.smartshop.smartshop.session.SessionUser;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;
    private final SessionService sessionService;

    private void requireAdmin() {
        sessionService.requireRole(UserRole.ADMIN);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders() {
        requireAdmin();
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        requireAdmin();
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponse>> getOrdersForClient(@PathVariable Long clientId) {
        requireAdmin();
        return ResponseEntity.ok(orderService.getOrdersForClient(clientId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {
        sessionService.requireRole(UserRole.CLIENT);
        SessionUser user = sessionService.getCurrentUser();
        ClientResponse client = clientService.getProfileForUser(user.getId());
        return ResponseEntity.ok(orderService.getOrdersForClient(client.getId()));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        requireAdmin();
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable Long id) {
        requireAdmin();
        return ResponseEntity.ok(orderService.confirmOrder(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long id) {
        requireAdmin();
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<OrderResponse> rejectOrder(@PathVariable Long id, @RequestBody(required = false) String reason) {
        requireAdmin();
        return ResponseEntity.ok(orderService.rejectOrder(id, reason));
    }
}


