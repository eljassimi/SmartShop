package com.smartshop.smartshop.services;

import com.smartshop.smartshop.dto.order.OrderItemRequest;
import com.smartshop.smartshop.dto.order.OrderRequest;
import com.smartshop.smartshop.dto.order.OrderResponse;
import com.smartshop.smartshop.exceptions.BusinessException;
import com.smartshop.smartshop.exceptions.ResourceNotFoundException;
import com.smartshop.smartshop.mappers.OrderMapper;
import com.smartshop.smartshop.models.client.Client;
import com.smartshop.smartshop.models.order.Order;
import com.smartshop.smartshop.models.order.OrderItem;
import com.smartshop.smartshop.models.order.OrderStatus;
import com.smartshop.smartshop.models.product.Product;
import com.smartshop.smartshop.repositories.ClientRepository;
import com.smartshop.smartshop.repositories.OrderRepository;
import com.smartshop.smartshop.repositories.ProductRepository;
import com.smartshop.smartshop.utils.MoneyUtils;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final LoyaltyService loyaltyService;

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        return orderMapper.toResponse(findOrderById(id));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersForClient(Long clientId) {
        Client client = findClientById(clientId);
        return orderMapper.toResponseList(orderRepository.findAllByClient(client));
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getItems())) {
            throw new BusinessException("An order must contain at least one item");
        }
        Client client = findClientById(request.getClientId());
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setClient(client);
        order.setStatus(OrderStatus.PENDING);
        order.setPromoCode(request.getPromoCode());
        order.setNotes(request.getNotes());
        OffsetDateTime now = OffsetDateTime.now();
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        List<OrderItem> orderItems = new ArrayList<>();
        double subtotal = 0;
        for (OrderItemRequest itemRequest : request.getItems()) {
            OrderItem orderItem = buildOrderItem(order, itemRequest);
            orderItems.add(orderItem);
            subtotal += orderItem.getLineTotal();
        }
        order.setItems(orderItems);
        order.setPayments(new ArrayList<>());

        double loyaltyDiscount = loyaltyService.calculateLoyaltyDiscount(client, subtotal);
        double promoDiscount = loyaltyService.calculatePromoDiscount(request.getPromoCode(), subtotal);
        double discount = MoneyUtils.round(Math.min(subtotal, loyaltyDiscount + promoDiscount));
        double netAmount = MoneyUtils.round(subtotal - discount);
        double vatRate = loyaltyService.getVatRatePercent();
        double vatAmount = MoneyUtils.round(netAmount * (vatRate / 100.0));
        double totalAmount = MoneyUtils.round(netAmount + vatAmount);

        order.setSubtotalAmount(MoneyUtils.round(subtotal));
        order.setDiscountAmount(discount);
        order.setNetAmount(netAmount);
        order.setVatRate(vatRate);
        order.setVatAmount(vatAmount);
        order.setTotalAmount(totalAmount);
        order.setRemainingAmount(totalAmount);

        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be confirmed");
        }
        if (MoneyUtils.round(order.getRemainingAmount()) > 0) {
            throw new BusinessException("Order cannot be confirmed until fully paid");
        }
        order.setStatus(OrderStatus.CONFIRMED);
        order.setUpdatedAt(OffsetDateTime.now());
        updateClientAfterConfirmation(order);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be canceled");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(OffsetDateTime.now());
        restoreStock(order);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse rejectOrder(Long orderId, String reason) {
        Order order = findOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException("Only pending orders can be rejected");
        }
        order.setStatus(OrderStatus.REJECTED);
        order.setNotes(reason);
        order.setUpdatedAt(OffsetDateTime.now());
        restoreStock(order);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    private OrderItem buildOrderItem(Order order, OrderItemRequest itemRequest) {
        if (itemRequest.getQuantity() == null || itemRequest.getQuantity() <= 0) {
            throw new BusinessException("Item quantity must be greater than zero");
        }
        Product product = findProductById(itemRequest.getProductId());
        if (Boolean.TRUE.equals(product.getDeleted())) {
            throw new BusinessException("Product " + product.getName() + " is not available");
        }
        if (product.getStockQuantity() < itemRequest.getQuantity()) {
            throw new BusinessException("Insufficient stock for product " + product.getName());
        }
        int quantity = itemRequest.getQuantity();
        double unitPrice = product.getUnitPrice();
        double lineTotal = MoneyUtils.round(unitPrice * quantity);
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(unitPrice);
        orderItem.setLineTotal(lineTotal);
        return orderItem;
    }

    private void updateClientAfterConfirmation(Order order) {
        Client client = order.getClient();
        int currentOrders = client.getTotalOrders() == null ? 0 : client.getTotalOrders();
        client.setTotalOrders(currentOrders + 1);
        double newTotalSpent = MoneyUtils.round(client.getTotalSpent() + order.getTotalAmount());
        client.setTotalSpent(newTotalSpent);
        if (client.getFirstOrderAt() == null) {
            client.setFirstOrderAt(order.getCreatedAt());
        }
        client.setLastOrderAt(order.getCreatedAt());
        loyaltyService.refreshTierForClient(client);
        clientRepository.save(client);
    }

    private void restoreStock(Order order) {
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        });
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    private Client findClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + id));
    }

    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

