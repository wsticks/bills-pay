package com.interswitch.user_management.model.request;

import com.interswitch.user_management.model.entity.Biller;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class CategoryRequest {

    private String categoryName;
    private List<Biller> billers;
}
