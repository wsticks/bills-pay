package com.interswitch.user_management.model.request;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class UpdateProductRequest {

    private String productName;
    private BigDecimal amount;
}
