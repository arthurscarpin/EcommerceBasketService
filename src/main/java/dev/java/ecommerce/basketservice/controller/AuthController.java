package dev.java.ecommerce.basketservice.controller;

import dev.java.ecommerce.basketservice.config.TokenService;
import dev.java.ecommerce.basketservice.controller.request.UserRequest;
import dev.java.ecommerce.basketservice.controller.response.LoginResponse;
import dev.java.ecommerce.basketservice.controller.response.UserResponse;
import dev.java.ecommerce.basketservice.entity.User;
import dev.java.ecommerce.basketservice.exception.DataNotFoundException;
import dev.java.ecommerce.basketservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("security/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody UserRequest request) {
        UsernamePasswordAuthenticationToken userAndPassword = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPassword);
        User user = (User) authentication.getPrincipal();
        String token = null;
        if (user != null) {
            token = tokenService.generateToken(user);
        } else {
            throw new DataNotFoundException("User not found");
        }
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
