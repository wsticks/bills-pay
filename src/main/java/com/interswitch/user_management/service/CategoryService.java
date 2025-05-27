package com.interswitch.user_management.service;

import com.interswitch.user_management.model.request.CategoryRequest;
import com.interswitch.user_management.model.request.UpdateCategoryRequest;
import com.interswitch.user_management.model.response.GlobalResponse;

public interface CategoryService {

    GlobalResponse createCategory(CategoryRequest categoryRequest);

    GlobalResponse updateCategory(UpdateCategoryRequest request, String categoryId);

    GlobalResponse getCategory(String categoryId);

    GlobalResponse fetchCategory();

    GlobalResponse deleteCategory(String categoryId);

}
