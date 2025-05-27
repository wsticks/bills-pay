package com.interswitch.user_management.model.response;

import com.interswitch.user_management.model.entity.Biller;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
public class CategoryResponse {

    private Long id;
    private String categoryName;
    private String categoryId;
    private List<BillerResponse> billers;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
