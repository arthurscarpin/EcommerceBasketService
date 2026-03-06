package dev.java.ecommerce.basketservice.service;

import dev.java.ecommerce.basketservice.client.response.PlatziProductResponse;
import dev.java.ecommerce.basketservice.controller.request.BasketRequest;
import dev.java.ecommerce.basketservice.controller.request.PaymentRequest;
import dev.java.ecommerce.basketservice.controller.request.ProductRequest;
import dev.java.ecommerce.basketservice.entity.Basket;
import dev.java.ecommerce.basketservice.entity.PaymentMethod;
import dev.java.ecommerce.basketservice.entity.Product;
import dev.java.ecommerce.basketservice.entity.Status;
import dev.java.ecommerce.basketservice.exception.BusinessException;
import dev.java.ecommerce.basketservice.exception.DataNotFoundException;
import dev.java.ecommerce.basketservice.repository.BasketRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private BasketRepository repository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private BasketService basketService;

    @Test
    @DisplayName("Should get by ID basket is OK")
    void getByIdCase1() {
        String id = "69a997dc355d5e04b2670abe";
        Product product1 = new Product(1L, "I phone", new BigDecimal(5500), 2);
        Product product2 = new Product(2L, "T-Shirt", new BigDecimal(80), 1);
        List<Product> products = List.of(product1, product2);
        Basket basket = new Basket(id, 1L, new BigDecimal(5630), products, Status.OPEN, PaymentMethod.DEBIT);
        when(repository.findById(id)).thenReturn(Optional.of(basket));

        Basket response = basketService.getById(id);

        assertEquals(basket, response);
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when basket not found")
    void getByIdCase2() {
        String id = "69a997dc355d5e04b2670abe";
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> basketService.getById(id));

        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Should create a new Basket")
    void createCase1() {
        ProductRequest productRequest1 = new ProductRequest(1L, 2);
        ProductRequest productRequest2 = new ProductRequest(2L, 1);
        List<ProductRequest> productRequest = List.of(productRequest1, productRequest2);
        BasketRequest request = new BasketRequest(1L, productRequest);
        when(repository.findByClientAndStatus(1L, Status.OPEN)).thenReturn(Optional.empty());
        when(repository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(productService.getById(1L)).thenReturn(new PlatziProductResponse(1L, "I Phone", new BigDecimal(5500)));
        when(productService.getById(2L)).thenReturn(new PlatziProductResponse(2L, "T-Shirt", new BigDecimal(80)));

        Basket response = basketService.create(request);

        assertEquals(Status.OPEN, response.getStatus());
        assertEquals(1L, response.getClient());
        assertEquals(2, response.getProducts().size());
        assertEquals("I Phone", response.getProducts().get(0).getTitle());
        assertEquals(2, response.getProducts().get(0).getQuantity());
        assertEquals("T-Shirt", response.getProducts().get(1).getTitle());
        assertEquals(1, response.getProducts().get(1).getQuantity());
        verify(repository).findByClientAndStatus(1L, Status.OPEN);
        verify(repository).save(any(Basket.class));
        verify(productService).getById(1L);
        verify(productService).getById(2L);
    }

    @Test
    @DisplayName("Should throw exception when client already has an open basket")
    void createCase2() {
        BasketRequest request = new BasketRequest(1L, List.of(new ProductRequest(1L, 2)));
        Basket basket = Basket.builder()
                .client(1L)
                .status(Status.OPEN)
                .build();
        when(repository.findByClientAndStatus(1L, Status.OPEN)).thenReturn(Optional.of(basket));

        BusinessException exception = assertThrows(BusinessException.class, () -> basketService.create(request));

        assertEquals("There is already an open basket for this client.", exception.getMessage());
        verify(repository).findByClientAndStatus(1L, Status.OPEN);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when a product is not found")
    void createCase3() {
        BasketRequest request = new BasketRequest(1L, List.of(new ProductRequest(999L, 2)));
        when(repository.findByClientAndStatus(1L, Status.OPEN)).thenReturn(Optional.empty());
        when(productService.getById(999L)).thenThrow(new DataNotFoundException("Product not found"));

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> basketService.create(request));

        assertEquals("Product not found", exception.getMessage());
        verify(repository).findByClientAndStatus(1L, Status.OPEN);
        verify(productService).getById(999L);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should update an existing basket successfully")
    void updateByIdCase1() {
        String id = "69a997dc355d5e04b2670abe";
        ProductRequest productRequest1 = new ProductRequest(1L, 2);
        ProductRequest productRequest2 = new ProductRequest(2L, 1);
        List<ProductRequest> products = List.of(productRequest1, productRequest2);
        BasketRequest request = new BasketRequest(1L, products);
        Basket existingBasket = new Basket(id, 1L, BigDecimal.ZERO, new ArrayList<>(), Status.OPEN, PaymentMethod.DEBIT);
        when(repository.findById(id)).thenReturn(Optional.of(existingBasket));
        when(productService.getById(1L)).thenReturn(new PlatziProductResponse(1L, "I Phone", new BigDecimal(5500)));
        when(productService.getById(2L)).thenReturn(new PlatziProductResponse(2L, "T-Shirt", new BigDecimal(80)));
        when(repository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Basket response = basketService.updateById(id, request);

        assertEquals(2, response.getProducts().size());
        assertEquals("I Phone", response.getProducts().get(0).getTitle());
        assertEquals(2, response.getProducts().get(0).getQuantity());
        assertEquals("T-Shirt", response.getProducts().get(1).getTitle());
        assertEquals(1, response.getProducts().get(1).getQuantity());
        verify(repository).findById(id);
        verify(productService).getById(1L);
        verify(productService).getById(2L);
        verify(repository).save(any(Basket.class));
    }

    @Test
    @DisplayName("Should throw exception if basket not found")
    void updateByIdCase2() {
        String id = "69a997dc355d5e04b2670abe";
        BasketRequest request = new BasketRequest(1L, List.of(new ProductRequest(1L, 1)));
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> basketService.updateById(id, request));
        verify(repository).findById(id);
        verify(productService, never()).getById(anyLong());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception if a product in request is not found")
    void updateByIdCase3() {
        String id = "69a997dc355d5e04b2670abe";
        ProductRequest product1 = new ProductRequest(999L, 1);
        BasketRequest request = new BasketRequest(1L, List.of(product1));
        Basket existingBasket = new Basket(id, 1L, BigDecimal.ZERO, new ArrayList<>(), Status.OPEN, PaymentMethod.DEBIT);
        when(repository.findById(id)).thenReturn(Optional.of(existingBasket));
        when(productService.getById(999L)).thenThrow(new DataNotFoundException("Product not found"));

        assertThrows(DataNotFoundException.class, () -> basketService.updateById(id, request));
        verify(repository).findById(id);
        verify(productService).getById(999L);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should pay the basket successfully")
    void payBasketCase1() {
        String id = "69a997dc355d5e04b2670abe";
        PaymentRequest request = new PaymentRequest(PaymentMethod.DEBIT);
        Basket existingBasket = new Basket(id, 1L, new BigDecimal(500), new ArrayList<>(), Status.OPEN, null);
        when(repository.findById(id)).thenReturn(Optional.of(existingBasket));
        when(repository.save(any(Basket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Basket response = basketService.payBasket(id, request);

        assertEquals(Status.SOLD, response.getStatus());
        assertEquals(PaymentMethod.DEBIT, response.getPaymentMethod());
        verify(repository).findById(id);
        verify(repository).save(existingBasket);
    }

    @Test
    @DisplayName("Should throw exception if basket not found when paying")
    void payBasketCase2() {
        String id = "69a997dc355d5e04b2670abe";
        PaymentRequest request = new PaymentRequest(PaymentMethod.CREDIT);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> basketService.payBasket(id, request));
        verify(repository).findById(id);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete basket successfully")
    void deleteByIdCase1() {
        String id = "69a997dc355d5e04b2670abe";
        Basket existingBasket = new Basket(id, 1L, new BigDecimal(500), new ArrayList<>(), Status.OPEN, null);

        when(repository.findById(id)).thenReturn(Optional.of(existingBasket));

        basketService.deleteById(id);
        verify(repository).findById(id);
        verify(repository).delete(existingBasket);
    }

    @Test
    @DisplayName("Should throw exception if basket not found when deleting")
    void deleteByIdCase2() {
        String id = "69a997dc355d5e04b2670abe";

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> basketService.deleteById(id));
        verify(repository).findById(id);
        verify(repository, never()).delete(any());
    }
}