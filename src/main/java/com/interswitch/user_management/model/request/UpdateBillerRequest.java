package com.interswitch.user_management.model.request;

import com.interswitch.user_management.model.entity.Category;
import com.interswitch.user_management.model.entity.Product;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class UpdateBillerRequest {

    private String billerName;
}
