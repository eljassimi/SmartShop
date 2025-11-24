package com.smartshop.smartshop.repositories;

import com.smartshop.smartshop.models.order.Order;
import com.smartshop.smartshop.models.order.OrderItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByOrder(Order order);
}

