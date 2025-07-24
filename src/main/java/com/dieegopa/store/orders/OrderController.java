package com.dieegopa.store.orders;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
@Tag(name = "Orders", description = "Operations related to orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(
            summary = "Get all orders",
            description = "Retrieves a list of all orders."
    )
    public List<OrderDto> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Get order by ID",
            description = "Retrieves an order by its unique ID. Returns 404 if the order does not exist."
    )
    public OrderDto getOrderById(
            @Parameter(
                    description = "The unique ID of the order to retrieve.",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "orderId") Long orderId
    ) {
        return orderService.getOrder(orderId);
    }
}
