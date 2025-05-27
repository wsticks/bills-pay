package com.interswitch.user_management.service;

import com.interswitch.user_management.model.request.ProductRequest;
import com.interswitch.user_management.model.request.UpdateProductRequest;
import com.interswitch.user_management.model.response.GlobalResponse;

public interface ProductService {

    GlobalResponse createProduct(ProductRequest productRequest);

    GlobalResponse updateProduct(UpdateProductRequest productRequest,
                                        String productId);

    GlobalResponse fetchProduct(String productId);

    GlobalResponse fetchProducts();

    GlobalResponse deleteProduct(String productId);
}
