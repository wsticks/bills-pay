package com.interswitch.user_management.controller;

import com.interswitch.user_management.model.request.CategoryRequest;
import com.interswitch.user_management.model.request.UpdateCategoryRequest;
import com.interswitch.user_management.model.response.GlobalResponse;
import com.interswitch.user_management.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/category")
public class categoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('CAN_CREATE_CATEGORY')")
    public GlobalResponse createCategory(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.createCategory(categoryRequest);
    }


    @PutMapping
    @PreAuthorize("hasRole('CAN_CREATE_CATEGORY')")
    public GlobalResponse updateCategory(@RequestBody UpdateCategoryRequest request,
                                       @RequestParam String categoryId) {
        return categoryService.updateCategory(request,categoryId);
    }

    @GetMapping
    @PreAuthorize("hasRole('CAN_FETCH_CATEGORY')")
    public GlobalResponse fetchCategory(@RequestParam String categoryId) {
        return categoryService.getCategory(categoryId);
    }

    @GetMapping("/fetch_all")
    @PreAuthorize("hasRole('CAN_FETCH_CATEGORIES')")
    public GlobalResponse fetchCategory() {
        return categoryService.fetchCategory();
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasRole('CAN_DELETE_CATEGORY')")
    public GlobalResponse deleteCategory(String categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
