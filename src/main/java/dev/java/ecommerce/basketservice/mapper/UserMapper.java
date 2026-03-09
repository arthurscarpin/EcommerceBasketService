package dev.java.ecommerce.basketservice.mapper;

import dev.java.ecommerce.basketservice.controller.request.UserRequest;
import dev.java.ecommerce.basketservice.controller.response.UserResponse;
import dev.java.ecommerce.basketservice.entity.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static User toUser(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .build();
    }

    public static UserResponse toUserResponse(User entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .build();
    }
}
