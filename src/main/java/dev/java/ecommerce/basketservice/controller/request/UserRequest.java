package dev.java.ecommerce.basketservice.controller.request;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
