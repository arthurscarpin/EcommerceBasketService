package dev.java.ecommerce.basketservice.documentation;

import dev.java.ecommerce.basketservice.client.response.PlatziProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(
        name = "Product",
        description = "Endpoint responsible to operations of Product."
)
public interface ProductControllerDoc {

    @Operation(
            summary = "Search all products",
            description = "Search all products on the Platzi API."
    )
    @ApiResponse(
            responseCode ="200",
            description = "Return all products."
    )
    ResponseEntity<List<PlatziProductResponse>> getAll();

    @Operation(
            summary = "Search a Product by ID",
            description = "Search a specific Product."
    )
    @ApiResponse(
            responseCode ="200",
            description = "Return product by ID."
    )
    ResponseEntity<PlatziProductResponse> getById(@PathVariable Long id);

}

