package com.coffeelink.bff.controller;

import com.coffeelink.bff.dto.BuyRequest;
import com.coffeelink.bff.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiCoreOrderUrl = "http://localhost:8080/pedidos";

    @PostMapping("/comprar/{productId}")
    public ResponseEntity<Object> buyProduct(
            @PathVariable Long productId,
            @RequestBody BuyRequest buyRequest,
            @AuthenticationPrincipal User user) {

        Map<String, Object> orderData = Map.of(
                "usuarioId", user.getId(),
                "productoId", productId,
                "cantidad", buyRequest.getCantidad()
        );

        try {
            Object newOrder = restTemplate.postForObject(apiCoreOrderUrl, orderData, Object.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getResponseBodyAsString());
            }
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }
}

