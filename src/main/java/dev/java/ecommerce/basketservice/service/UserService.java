package dev.java.ecommerce.basketservice.service;

import dev.java.ecommerce.basketservice.controller.request.UserRequest;
import dev.java.ecommerce.basketservice.controller.response.UserResponse;
import dev.java.ecommerce.basketservice.entity.User;
import dev.java.ecommerce.basketservice.mapper.UserMapper;
import dev.java.ecommerce.basketservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserResponse save(UserRequest request) {
        User user = UserMapper.toUser(request);
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        return UserMapper.toUserResponse(repository.save(user));
    }
}
