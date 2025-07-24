package com.dieegopa.store.products;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
@Tag(name = "Product", description = "Operations related to product management")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Retrieves a list of all products, optionally filtered by category ID."
    )
    public Iterable<ProductDto> getAllProducts(
            @Parameter(
                    description = "Category ID to filter products. If not provided or 0, all products are returned.",
                    example = "1"
            )
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
    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a product by its unique ID. Returns 404 if the product does not exist."
    )
    public ResponseEntity<ProductDto> getProduct(
            @Parameter(
                    description = "The unique ID of the product to retrieve.",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(productMapper.toDto(product));
    }

    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product with the provided details. The category must exist."
    )
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder
    ) {

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);

        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update product details",
            description = "Updates the details of an existing product. The category must exist."
    )
    public ResponseEntity<ProductDto> updateProduct(
            @Parameter(
                    description = "The unique ID of the product to update.",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id,
            @RequestBody ProductDto productDto
    ) {

        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a product",
            description = "Deletes a product by its unique ID. Returns 404 if the product does not exist."
    )
    public ResponseEntity<Void> deleteProduct(
            @Parameter(
                    description = "The unique ID of the product to delete.",
                    required = true,
                    example = "1"
            )
            @PathVariable(name = "id") Long id
    ) {
        var product = productRepository.findById(id).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
