package com.smartshop.smartshop.services;

import com.smartshop.smartshop.dto.client.ClientRequest;
import com.smartshop.smartshop.dto.client.ClientResponse;
import com.smartshop.smartshop.exceptions.BusinessException;
import com.smartshop.smartshop.exceptions.ResourceNotFoundException;
import com.smartshop.smartshop.mappers.ClientMapper;
import com.smartshop.smartshop.models.client.Client;
import com.smartshop.smartshop.models.client.CustomerTier;
import com.smartshop.smartshop.repositories.ClientRepository;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final LoyaltyService loyaltyService;

    @Transactional(readOnly = true)
    public List<ClientResponse> getAllClients() {
        return clientMapper.toResponseList(clientRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ClientResponse getClient(Long id) {
        return clientMapper.toResponse(findClientById(id));
    }

    @Transactional
    public ClientResponse createClient(ClientRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Client email already exists");
        }
        Client client = clientMapper.toEntity(request);
        OffsetDateTime now = OffsetDateTime.now();
        client.setTier(CustomerTier.BASIC);
        client.setTotalOrders(0);
        client.setTotalSpent(0);
        client.setCreatedAt(now);
        client.setUpdatedAt(now);
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional
    public ClientResponse updateClient(Long id, ClientRequest request) {
        Client client = findClientById(id);
        if (!client.getEmail().equalsIgnoreCase(request.getEmail())
                && clientRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Client email already exists");
        }
        clientMapper.updateEntityFromRequest(request, client);
        client.setUpdatedAt(OffsetDateTime.now());
        loyaltyService.refreshTierForClient(client);
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = findClientById(id);
        clientRepository.delete(client);
    }

    @Transactional(readOnly = true)
    public ClientResponse getProfileForUser(Long userId) {
        return clientMapper.toResponse(findClientByUserId(userId));
    }

    Client findClientByUserId(Long userId) {
        return clientRepository.findByUserAccountId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found for user " + userId));
    }

    Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
    }
}

