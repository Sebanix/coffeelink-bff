package com.coffeelink.bff.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiCoreUrl = "http://localhost:8080/productos";

    public Object getAllProducts(String nombre, BigDecimal precioMin, BigDecimal precioMax, Pageable pageable) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiCoreUrl);

        if (nombre != null) {
            builder.queryParam("nombre", nombre);
        }
        if (precioMin != null) {
            builder.queryParam("precioMin", precioMin);
        }
        if (precioMax != null) {
            builder.queryParam("precioMax", precioMax);
        }

        builder.queryParam("page", pageable.getPageNumber());
        builder.queryParam("size", pageable.getPageSize());

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                builder.queryParam("sort", order.getProperty() + "," + order.getDirection().name());
            }
        }

        return restTemplate.getForObject(builder.toUriString(), Object.class);
    }

    public Object getProductById(String id) {
        return restTemplate.getForObject(apiCoreUrl + "/" + id, Object.class);
    }

    public Object createProduct(Object productData) {
        return restTemplate.postForObject(apiCoreUrl, productData, Object.class);
    }

    public void updateProduct(String id, Object productData) {
        restTemplate.put(apiCoreUrl + "/" + id, productData);
    }

    public void deleteProduct(String id) {
        restTemplate.delete(apiCoreUrl + "/" + id);
    }
}