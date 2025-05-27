package com.interswitch.user_management.controller;

import com.interswitch.user_management.model.request.ProductRequest;
import com.interswitch.user_management.model.request.UpdateProductRequest;
import com.interswitch.user_management.model.response.GlobalResponse;
import com.interswitch.user_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('CAN_CREATE_PRODUCT')")
    public GlobalResponse createProduct(@RequestBody ProductRequest productRequest) {
        return productService.createProduct(productRequest);
    }


    @PutMapping
    @PreAuthorize("hasRole('CAN_UPDATE_PRODUCT')")
    public GlobalResponse updateProduct(@RequestBody UpdateProductRequest productRequest,
                                        @RequestParam String productId) {
        return productService.updateProduct(productRequest,productId);
    }

    @GetMapping
    @PreAuthorize("hasRole('CAN_FETCH_PRODUCT')")
    public GlobalResponse fetchProduct(String productId) {
        return productService.fetchProduct(productId);
    }

    @GetMapping("fetch_all")
    @PreAuthorize("hasRole('CAN_FETCH_PRODUCTS')")
    public GlobalResponse fetchProducts() {
        return productService.fetchProducts();
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasRole('CAN_DELETE_PRODUCT')")
    public GlobalResponse deleteProduct(String productId) {
        return productService.deleteProduct(productId);
    }
}
