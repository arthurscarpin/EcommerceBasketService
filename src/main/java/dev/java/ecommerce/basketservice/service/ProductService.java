package dev.java.ecommerce.basketservice.service;

import dev.java.ecommerce.basketservice.client.PlatziStoreClient;
import dev.java.ecommerce.basketservice.client.response.PlatziProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final PlatziStoreClient client;

    @Cacheable(value = "products")
    public List<PlatziProductResponse> getAll() {
        log.info("Getting all products");
        return client.getAll();
    }

    @Cacheable(value = "product", key = "#productId")
    public PlatziProductResponse getById(Long productId) {
        log.info("Getting product with id: {}", productId);
        return client.getById(productId);
    }

}
