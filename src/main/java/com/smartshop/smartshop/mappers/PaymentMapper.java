package com.smartshop.smartshop.mappers;

import com.smartshop.smartshop.dto.payment.PaymentRequest;
import com.smartshop.smartshop.dto.payment.PaymentResponse;
import com.smartshop.smartshop.models.payment.Payment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Payment toEntity(PaymentRequest request);
}

