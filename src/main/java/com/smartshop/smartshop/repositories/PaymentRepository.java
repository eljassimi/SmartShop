package com.smartshop.smartshop.repositories;

import com.smartshop.smartshop.models.order.Order;
import com.smartshop.smartshop.models.payment.Payment;
import com.smartshop.smartshop.models.payment.PaymentStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findAllByOrder(Order order);

    long countByOrderAndStatus(Order order, PaymentStatus status);
}

