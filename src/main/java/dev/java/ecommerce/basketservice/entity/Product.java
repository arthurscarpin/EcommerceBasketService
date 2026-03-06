package dev.java.ecommerce.basketservice.entity;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    private Long id;

    private String title;

    private BigDecimal price;

    private Integer quantity;
}
