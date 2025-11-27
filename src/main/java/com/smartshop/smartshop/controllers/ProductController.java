package com.smartshop.smartshop.controllers;

import com.smartshop.smartshop.dto.product.ProductRequest;
import com.smartshop.smartshop.dto.product.ProductResponse;
import com.smartshop.smartshop.models.user.UserRole;
import com.smartshop.smartshop.services.ProductService;
import com.smartshop.smartshop.session.SessionService;
import com.smartshop.smartshop.session.SessionUser;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final SessionService sessionService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(name = "includeDeleted", defaultValue = "false") boolean includeDeleted) {
        SessionUser user = sessionService.getCurrentUser();
        boolean canViewDeleted = user.getRole() == UserRole.ADMIN;
        return ResponseEntity.ok(productService.getAllProducts(canViewDeleted && includeDeleted));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        sessionService.getCurrentUser(); // ensure authenticated
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        sessionService.requireRole(UserRole.ADMIN);
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        sessionService.requireRole(UserRole.ADMIN);
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        sessionService.requireRole(UserRole.ADMIN);
        productService.softDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}


