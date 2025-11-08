package com.coffeelink.bff.controller;

import com.coffeelink.bff.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Publcos

    @GetMapping("/productos")
    public ResponseEntity<Object> getProducts() {
        Object products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Object> getProductsById(@PathVariable String id) {
        Object product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Admin

    @PostMapping("/productos")
    public ResponseEntity<Object> createProduct(@RequestBody Object productData) {
        Object newProduct = productService.createProduct(productData);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @PutMapping("/productos/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable String id, @RequestBody Object productData) {
        productService.updateProduct(id, productData);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/productos/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
