package dev.java.ecommerce.basketservice.controller.request;

import dev.java.ecommerce.basketservice.entity.PaymentMethod;

public record PaymentRequest(
        PaymentMethod paymentMethod
) {
}
