package com.smartshop.smartshop.mappers;

import com.smartshop.smartshop.dto.order.OrderItemResponse;
import com.smartshop.smartshop.models.order.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    OrderItemResponse toResponse(OrderItem item);

    List<OrderItemResponse> toResponseList(List<OrderItem> items);
}

