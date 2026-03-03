package dev.java.ecommerce.basketservice.service;

import dev.java.ecommerce.basketservice.client.PlatziStoreClient;
import dev.java.ecommerce.basketservice.client.response.PlatziProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final PlatziStoreClient client;

    public List<PlatziProductResponse> getAll() {
        return client.getAll();
    }

    public PlatziProductResponse getById(Long id) {
        return client.getById(id);
    }

}
