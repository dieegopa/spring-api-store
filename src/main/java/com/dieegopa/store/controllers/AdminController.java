package com.dieegopa.store.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Operations related to admin functionalities")
public class AdminController {

    @GetMapping("/hello")
    @Operation(
            summary = "Admin greeting",
            description = "Returns a greeting message for admin users."
    )
    public String sayHello() {
        return "Hello, Admin!";
    }
}
