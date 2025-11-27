package com.smartshop.smartshop.services;

import com.smartshop.smartshop.dto.payment.PaymentRequest;
import com.smartshop.smartshop.dto.payment.PaymentResponse;
import com.smartshop.smartshop.exceptions.BusinessException;
import com.smartshop.smartshop.exceptions.ResourceNotFoundException;
import com.smartshop.smartshop.mappers.PaymentMapper;
import com.smartshop.smartshop.models.order.Order;
import com.smartshop.smartshop.models.order.OrderStatus;
import com.smartshop.smartshop.models.payment.Payment;
import com.smartshop.smartshop.repositories.OrderRepository;
import com.smartshop.smartshop.repositories.PaymentRepository;
import com.smartshop.smartshop.utils.MoneyUtils;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponse recordPayment(PaymentRequest request) {
        Order order = findOrderById(request.getOrderId());
        validateOrderStatus(order);
        if (request.getAmount() <= 0) {
            throw new BusinessException("Payment amount must be greater than zero");
        }
        if (MoneyUtils.round(request.getAmount()) > MoneyUtils.round(order.getRemainingAmount())) {
            throw new BusinessException("Payment exceeds remaining amount");
        }
        Payment payment = paymentMapper.toEntity(request);
        payment.setOrder(order);
        payment.setPaymentNumber((int) paymentRepository.countByOrder(order) + 1);
        payment.setCreatedAt(OffsetDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);

        double newRemaining = MoneyUtils.round(order.getRemainingAmount() - payment.getAmount());
        order.setRemainingAmount(Math.max(newRemaining, 0));
        order.setUpdatedAt(OffsetDateTime.now());
        orderRepository.save(order);
        return paymentMapper.toResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsForOrder(Long orderId) {
        Order order = findOrderById(orderId);
        List<Payment> payments = paymentRepository.findAllByOrder(order);
        return paymentMapper.toResponseList(payments);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    private void validateOrderStatus(Order order) {
        if (order.getStatus() == OrderStatus.CANCELED || order.getStatus() == OrderStatus.REJECTED) {
            throw new BusinessException("Payments cannot be recorded for canceled or rejected orders");
        }
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            throw new BusinessException("Order is already confirmed");
        }
    }
}

