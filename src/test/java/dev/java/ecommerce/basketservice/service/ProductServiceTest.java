package dev.java.ecommerce.basketservice.service;

import dev.java.ecommerce.basketservice.client.PlatziStoreClient;
import dev.java.ecommerce.basketservice.client.response.PlatziProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private PlatziStoreClient client;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should get all products is OK")
    void getAllCase1() {
        // Arrange
        PlatziProductResponse product1 = new PlatziProductResponse(1L, "I Phone", new BigDecimal(5500));
        PlatziProductResponse product2 = new PlatziProductResponse(2L, "Laptop", new BigDecimal(4000));
        PlatziProductResponse product3 = new PlatziProductResponse(3L, "T-Shirt", new BigDecimal(55));
        List<PlatziProductResponse> products = List.of(product1, product2, product3);
        when(client.getAll()).thenReturn(products);

        // Act
        List<PlatziProductResponse> response = productService.getAll();

        // Assert
        assertEquals(3, response.size());
        assertEquals(products, response);
        verify(client).getAll();
    }

    @Test
    @DisplayName("Should get by ID product is OK")
    void getByIdCase1() {
        // Arrange
        Long productId = 1L;
        PlatziProductResponse product = new PlatziProductResponse(1L, "I Phone", new BigDecimal(5500));
        when(client.getById(productId)).thenReturn(product);

        // Act
        PlatziProductResponse response = productService.getById(productId);

        // Assert
        assertEquals(product, response);
        verify(client).getById(productId);
    }
}