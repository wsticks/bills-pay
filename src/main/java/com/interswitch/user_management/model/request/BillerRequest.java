package com.interswitch.user_management.model.request;

import com.interswitch.user_management.model.entity.Product;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class BillerRequest {

    private String billerId;
    private String billerName;
    private List<Product> productList;
    private String categoryId;

}
//biller: billerId,billerNmae,List of Products, categoryId,