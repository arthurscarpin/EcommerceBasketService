package dev.java.ecommerce.basketservice.service;

import dev.java.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.java.ecommerce.basketservice.controller.request.BasketRequest;
import dev.java.ecommerce.basketservice.entity.Basket;
import dev.java.ecommerce.basketservice.entity.Product;
import dev.java.ecommerce.basketservice.entity.Status;
import dev.java.ecommerce.basketservice.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository repository;

    private final ProductService productService;

    public Basket getById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Basket not found!"));
    }

    public Basket create(BasketRequest request) {
        repository.findByClientAndStatus(request.clientId(), Status.OPEN)
                .ifPresent(basket -> {
                    throw new IllegalArgumentException("There is already an open basket for this client.");
                });

        List<Product> products = new ArrayList<>();

        request.products().forEach(product -> {
            PlatziProductResponse platziProductResponse = productService.getById(product.id());
            products.add(Product.builder()
                    .id(platziProductResponse.id())
                    .title(platziProductResponse.title())
                    .price(platziProductResponse.price())
                    .quantity(product.quantity())
                    .build());
        });

        Basket basket = Basket.builder()
                .client(request.clientId())
                .status(Status.OPEN)
                .products(products)
                .build();

        basket.calculateTotalPrice();
        return repository.save(basket);
    }
}
