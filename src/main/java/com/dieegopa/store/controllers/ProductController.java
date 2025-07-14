package com.dieegopa.store.controllers;

import com.dieegopa.store.dtos.ProductDto;
import com.dieegopa.store.entities.Product;
import com.dieegopa.store.mappers.ProductMapper;
import com.dieegopa.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public Iterable<ProductDto> getAllProducts(
            @RequestParam(name = "categoryId", required = false, defaultValue = "0") Byte categoryId
    ) {

        List<Product> products = (categoryId == null || categoryId <= 0)
                ? productRepository.findAllWithCategory()
                : productRepository.findByCategoryId(categoryId);

        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product));
    }
}
