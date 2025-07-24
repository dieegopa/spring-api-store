package com.dieegopa.store.carts;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/carts")
@Tag(name = "Cart", description = "Operations related to shopping carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    @Operation(
            summary = "Create a new cart",
            description = "Creates a new shopping cart. The cart is initially empty."
    )
    public ResponseEntity<CartDto> createCart() {

        var cartDto = cartService.createCart();

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    @Operation(
            summary = "Add an item to the cart",
            description = "Adds a product to the specified cart by its ID. The product must exist in the system."
    )
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(
                    description = "The ID of the cart to which the item will be added",
                    required = true
            )
            @PathVariable(value = "cartId") String cartId,
            @Valid @RequestBody AddItemToCartRequest request
    ) {
        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    @Operation(
            summary = "Get cart details",
            description = "Retrieves the details of a shopping cart by its ID, including all items and total price."
    )
    public CartDto getCart(
            @Parameter(
                    description = "The ID of the cart to retrieve",
                    required = true
            )
            @PathVariable(value = "cartId") String cartId
    ) {

        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    @Operation(
            summary = "Update item quantity in the cart",
            description = "Updates the quantity of a specific product in the cart. If the product does not exist in the cart, it returns an error."
    )
    public CartItemDto updateItem(
            @Parameter(
                    description = "The ID of the cart containing the item",
                    required = true
            )
            @PathVariable(value = "cartId") String cartId,
            @Parameter(
                    description = "The ID of the product to update in the cart",
                    required = true
            )
            @PathVariable(value = "productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    @Operation(
            summary = "Remove an item from the cart",
            description = "Removes a specific product from the cart by its ID. If the product does not exist in the cart, it does nothing."
    )
    public ResponseEntity<?> removeItem(
            @Parameter(
                    description = "The ID of the cart from which the item will be removed",
                    required = true
            )
            @PathVariable(name = "cartId") String cartId,
            @Parameter(
                    description = "The ID of the product to remove from the cart",
                    required = true
            )
            @PathVariable(name = "productId") Long productId
    ) {
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    @Operation(
            summary = "Clear the cart",
            description = "Removes all items from the specified cart, leaving it empty."
    )
    public ResponseEntity<Void> clearCart(
            @Parameter(
                    description = "The ID of the cart to clear",
                    required = true
            )
            @PathVariable(name = "cartId") String cartId
    ) {
        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();
    }
}
