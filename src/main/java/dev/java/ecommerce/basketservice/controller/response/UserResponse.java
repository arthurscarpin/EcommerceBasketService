package dev.java.ecommerce.basketservice.controller.response;

import lombok.Builder;

@Builder
public record UserResponse(
        String id,
        String name,
        String email
) {
}
