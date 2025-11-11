package com.coffeelink.bff.controller;

import com.coffeelink.bff.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import java.math.BigDecimal;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/productos")
    public ResponseEntity<Object> getProducts(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            Pageable pageable
    ) {
        Object products = productService.getAllProducts(nombre, precioMin, precioMax, pageable);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Object> getProductById(@PathVariable String id) {
        Object product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/productos")
    public ResponseEntity<Object> createProduct(@RequestBody Object productData) {
        try {
            Object newProduct = productService.createProduct(productData);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        }
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody Object productData) {
        try {
            productService.updateProduct(id, productData);
            return ResponseEntity.ok().build();

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(null);
        }
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}