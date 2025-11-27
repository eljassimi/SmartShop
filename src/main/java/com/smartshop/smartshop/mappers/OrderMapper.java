package com.smartshop.smartshop.mappers;

import com.smartshop.smartshop.dto.order.OrderResponse;
import com.smartshop.smartshop.models.order.Order;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientCompanyName", source = "client.companyName")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "payments", source = "payments")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}

