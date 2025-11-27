package com.smartshop.smartshop.controllers;

import com.smartshop.smartshop.dto.client.ClientRequest;
import com.smartshop.smartshop.dto.client.ClientResponse;
import com.smartshop.smartshop.models.user.UserRole;
import com.smartshop.smartshop.services.ClientService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final SessionService sessionService;

    private void requireAdmin() {
        sessionService.requireRole(UserRole.ADMIN);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getClients() {
        requireAdmin();
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClient(@PathVariable Long id) {
        requireAdmin();
        return ResponseEntity.ok(clientService.getClient(id));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientRequest request) {
        requireAdmin();
        return ResponseEntity.ok(clientService.createClient(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponse> updateClient(
            @PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        requireAdmin();
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        requireAdmin();
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ClientResponse> getMyProfile() {
        sessionService.requireRole(UserRole.CLIENT);
        SessionUser user = sessionService.getCurrentUser();
        return ResponseEntity.ok(clientService.getProfileForUser(user.getId()));
    }
}


