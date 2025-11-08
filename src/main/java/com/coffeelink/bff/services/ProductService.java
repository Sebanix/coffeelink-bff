package com.coffeelink.bff.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {

    @Autowired
    private RestTemplate restTemplate;

    private final String apiCoreUrl = "http://localhost:8080/productos";

    public Object getAllProducts() {
        return restTemplate.getForObject(apiCoreUrl, Object.class);
    }

    public Object getProductById(String id) {
        return restTemplate.getForObject(apiCoreUrl + "/" + id, Object.class);
    }

    public Object createProduct(Object productData){
        return restTemplate.postForObject(apiCoreUrl, productData, Object.class);
    }

    public void updateProduct(String id, Object productData){
        restTemplate.put(apiCoreUrl + "/" + id, productData);
    }

    public void deleteProduct(String id){
        restTemplate.delete(apiCoreUrl + "/" + id);
    }
}
