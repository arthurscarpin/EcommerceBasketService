package dev.java.ecommerce.basketservice.documentation;

import dev.java.ecommerce.basketservice.controller.request.BasketRequest;
import dev.java.ecommerce.basketservice.controller.request.PaymentRequest;
import dev.java.ecommerce.basketservice.entity.Basket;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(
        name = "Basket",
        description = "Endpoint responsible to operations of Basket."
)
public interface BasketControllerDoc {

    @Operation(
            summary = "Search a Basket by ID",
            description = "Search a specific Basket."
    )
    @ApiResponse(
            responseCode ="200",
            description = "Return basket by ID."
    )
    @ApiResponse(
            responseCode ="404",
            description = "Return the Business Exception: Basket not found."
    )
    ResponseEntity<Basket> getById(@PathVariable String id);

    @Operation(
            summary = "Create a Basket",
            description = "Create a basket of according products."
    )
    @ApiResponse(
            responseCode ="201",
            description = "Return basket created."
    )
    @ApiResponse(
            responseCode ="400",
            description = "Return the Business Exception: There is already an open basket for this client."
    )
    ResponseEntity<Basket> create(@RequestBody BasketRequest request);

    @Operation(
            summary = "Update a Basket",
            description = "Update a basket of according products."
    )
    @ApiResponse(
            responseCode ="200",
            description = "Return basket updated."
    )
    @ApiResponse(
            responseCode ="404",
            description = "Return the Business Exception: Basket not found."
    )
    ResponseEntity<Basket> updateById(@PathVariable String id, @RequestBody BasketRequest request);

    @Operation(
            summary = "Update a payment of Basket",
            description = "Update a payment of basket of according products."
    )
    @ApiResponse(
            responseCode ="200",
            description = "Return payment basket updated."
    )
    @ApiResponse(
            responseCode ="404",
            description = "Return the Business Exception: Basket not found."
    )
    ResponseEntity<Basket> updateByIdPayBasket(@PathVariable String id, @RequestBody PaymentRequest request);

    @Operation(
            summary = "Delete a Basket by ID",
            description = "Update a payment of basket of according products."
    )
    @ApiResponse(
            responseCode ="204",
            description = "No Content."
    )
    ResponseEntity<Basket> deleteById(@PathVariable String id);

}

