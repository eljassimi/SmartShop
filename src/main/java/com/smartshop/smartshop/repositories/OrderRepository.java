package com.smartshop.smartshop.repositories;

import com.smartshop.smartshop.models.client.Client;
import com.smartshop.smartshop.models.order.Order;
import com.smartshop.smartshop.models.order.OrderStatus;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findAllByClient(Client client);

    long countByClientAndStatus(Client client, OrderStatus status);

    List<Order> findAllByClientAndStatus(Client client, OrderStatus status);

    List<Order> findAllByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);

    @Query("select coalesce(sum(o.totalAmount), 0) from Order o where o.client = :client and o.status = :status")
    double sumTotalAmountByClientAndStatus(@Param("client") Client client, @Param("status") OrderStatus status);
}

