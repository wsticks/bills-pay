package com.interswitch.user_management.model.request;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class ProductRequest {

    private String productId;
    private String productName;
    private BigDecimal amount;
    private String billerId;
    private String categoryId;

}
//amount ,productname, productId, billerId,categoryId
//
//biller: billerId,billerNmae,List of Products, categoryId,
//
//category: categoryId, categoryName,List of billers,