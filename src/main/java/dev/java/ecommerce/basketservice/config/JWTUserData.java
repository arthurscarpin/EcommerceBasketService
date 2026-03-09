package dev.java.ecommerce.basketservice.config;

import lombok.Builder;

@Builder
public record JWTUserData(
        Long id,
        String name,
        String email,
        String password,
        String role
) {
}
