package com.interswitch.user_management.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.user_management.constant.GlobalConstant;
import com.interswitch.user_management.enums.GlobalEnum;
import com.interswitch.user_management.model.entity.Product;
import com.interswitch.user_management.model.request.ProductRequest;
import com.interswitch.user_management.model.request.UpdateProductRequest;
import com.interswitch.user_management.model.response.GlobalResponse;
import com.interswitch.user_management.model.response.ProductResponse;
import com.interswitch.user_management.repository.ProductRepository;
import com.interswitch.user_management.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ObjectMapper objectMapper;

    public GlobalResponse createProduct(ProductRequest productRequest){
        Map<Object,Object> data = new HashMap<>();
        try{
        ProductResponse productResponse;
        Product product;
        product = objectMapper.convertValue(productRequest,Product.class);
        product = productRepository.save(product);
        product = productRepository.save(product);
        productResponse = objectMapper.convertValue(product,ProductResponse.class);
        data.put("product",productResponse);
        log.info("Success:::: Product created successfully with response: {}", productResponse);
        return new GlobalResponse(GlobalEnum.S0000.getValue(),
                GlobalConstant.Product_Created_Successfully,
                data);
        } catch (Exception e){
            log.error("ERROR ::: product creation failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Creating_Product,
                    data);
        }
    }

    public GlobalResponse updateProduct(UpdateProductRequest productRequest,
                                        String productId){
        Map<Object,Object> data = new HashMap<>();
        try{
            ProductResponse productResponse;
            Product product;
            product = productRepository.findByProductId(productId);
            if(product==null){
                return new GlobalResponse(
                        GlobalEnum.E0000.getValue(),
                        GlobalConstant.Product_Not_Found,
                        data
                );
            }
            product = productRepository.save(product);
            productResponse = objectMapper.convertValue(product,ProductResponse.class);
            data.put("updateProduct",productResponse);
            log.info("Success:::: Product updated successfully with response: {}", productResponse);
            return new GlobalResponse(GlobalEnum.S0000.getValue(),
                    GlobalConstant.Product_Updated_Successfully,
                    data);
        } catch (Exception e){
            log.error("ERROR ::: product update failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Updating_Product,
                    data);
        }
    }

    public GlobalResponse fetchProduct(String productId){
        Map<Object,Object> data = new HashMap<>();
        try{
            Product fetchedProduct = productRepository.findByProductId(productId);
      if (fetchedProduct != null) {
          ProductResponse productResponse = objectMapper.convertValue(fetchedProduct,
                  ProductResponse.class);
        data.put("fetchedProduct", productResponse);
        return new GlobalResponse(GlobalEnum.S0000.getValue(),
                GlobalConstant.Product_Fetched_Successfully,
                data);
      } else{
          return new GlobalResponse(GlobalEnum.E0000.getValue(),
                  GlobalConstant.Error_Fetching_Product,
                  data);
      }
        } catch (Exception e){
            log.error("ERROR ::: product fetch failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Fetching_Product,
                    data);

        }
    }

    public GlobalResponse fetchProducts(){
        Map<Object,Object> data = new HashMap<>();
        try{
            List<Product> fetchedProducts = productRepository.findAll();
                List<ProductResponse> productResponses = objectMapper.convertValue(
                        fetchedProducts,
                        new TypeReference<List<ProductResponse>>() {}
                );
                data.put("fetchedProducts", productResponses);
                return new GlobalResponse(GlobalEnum.S0000.getValue(),
                        GlobalConstant.Products_Fetched_Successfully,
                        data);
        } catch (Exception e){
            log.error("ERROR ::: product fetch failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Fetching_Products,
                    data);

        }
    }

    public GlobalResponse deleteProduct(String productId) {
        Map<Object, Object> data = new HashMap<>();
        try {
            Product product = productRepository.findByProductId(productId);
            if (product != null) {
                productRepository.delete(product);
                data.put("deletedProductId", productId);
                return new GlobalResponse(
                        GlobalEnum.S0000.getValue(),
                        GlobalConstant.Product_Deleted_Successfully,
                        data
                );
            } else {
                return new GlobalResponse(
                        GlobalEnum.E0000.getValue(),
                        GlobalConstant.Error_Deleting_Product,
                        data
                );
            }
        } catch (Exception e) {
            log.error("ERROR ::: product delete failed : {}", e.getMessage(), e);
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Deleting_Product,
                    data
            );
        }
    }
}
