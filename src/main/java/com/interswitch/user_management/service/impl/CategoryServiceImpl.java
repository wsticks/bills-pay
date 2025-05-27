package com.interswitch.user_management.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.user_management.constant.GlobalConstant;
import com.interswitch.user_management.enums.GlobalEnum;
import com.interswitch.user_management.model.entity.Biller;
import com.interswitch.user_management.model.entity.Category;
import com.interswitch.user_management.model.request.BillerRequest;
import com.interswitch.user_management.model.request.CategoryRequest;
import com.interswitch.user_management.model.request.UpdateCategoryRequest;
import com.interswitch.user_management.model.response.BillerResponse;
import com.interswitch.user_management.model.response.CategoryResponse;
import com.interswitch.user_management.model.response.GlobalResponse;
import com.interswitch.user_management.repository.BillerRepository;
import com.interswitch.user_management.repository.CategoryRepository;
import com.interswitch.user_management.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public GlobalResponse createCategory(CategoryRequest categoryRequest) {
        Map<Object,Object> data = new HashMap<>();
        try {
            Category category = new Category();
            category.setCategoryName(categoryRequest.getCategoryName());
            category.setCategoryId(UUID.randomUUID().toString());

            List<Biller> billers = new ArrayList<>();
            for (Biller billerRequest : categoryRequest.getBillers()) {
                Biller biller = new Biller();
                biller.setBillerName(billerRequest.getBillerName());
                biller.setBillerId(UUID.randomUUID().toString());
                biller.setCategory(category);
                billers.add(biller);
            }

            category.setBillers(billers);
            category = categoryRepository.save(category);

            CategoryResponse categoryResponse = objectMapper.convertValue(category, CategoryResponse.class);
            data.put("category", categoryResponse);

            log.info("Success:::: Category created successfully with response: {}", categoryResponse);
            return new GlobalResponse(GlobalEnum.S0000.getValue(),
                    GlobalConstant.Category_Created_Successfully,
                    data);
        } catch (Exception e) {
            log.error("ERROR ::: Category creation failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Creating_Category,
                    data);
        }
    }

    public GlobalResponse updateCategory(UpdateCategoryRequest request, String categoryId) {
        Map<Object, Object> data = new HashMap<>();
        try {
            Category existingCategory = categoryRepository.findByCategoryId(categoryId);
            if (existingCategory == null) {
                return new GlobalResponse(
                        GlobalEnum.E0000.getValue(),
                        GlobalConstant.Category_Not_Found,
                        data
                );
            }
            existingCategory.setCategoryName(request.getCategoryName());
            existingCategory.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            Category updatedBiller = categoryRepository.save(existingCategory);
            BillerResponse response = objectMapper.convertValue(updatedBiller, BillerResponse.class);

            data.put("updatedCategory", response);
            log.info("Success ::: Category updated successfully with response: {}", response);

            return new GlobalResponse(
                    GlobalEnum.S0000.getValue(),
                    GlobalConstant.Category_Updated_Successfully,
                    data
            );

        } catch (Exception e) {
            log.error("ERROR ::: Category update failed : {}", e.getMessage(), e);
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Updating_Category,
                    data
            );
        }
    }

    public GlobalResponse getCategory(String categoryId){
        Map<Object,Object> data = new HashMap<>();
        try{
            Category fetchedCategory = categoryRepository.findCategoryWithBillers(categoryId);
            if (fetchedCategory != null) {
//
                data.put("fetchedCategory", fetchedCategory);
                return new GlobalResponse(GlobalEnum.S0000.getValue(),
                        GlobalConstant.Category_Fetched_Successfully,
                        data);
            } else{
                return new GlobalResponse(GlobalEnum.E0000.getValue(),
                        GlobalConstant.Error_Fetching_Category,
                        data);
            }
        } catch (Exception e){
            log.error("ERROR ::: Fetching category failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Fetching_Category,
                    data);

        }
    }

    public GlobalResponse fetchCategory(){
        Map<Object,Object> data = new HashMap<>();
        try{
            List<Category> fetchBillers = categoryRepository.findAll();
            List<CategoryResponse> billerResponseList = objectMapper.convertValue(
                    fetchBillers,
                    new TypeReference<List<CategoryResponse>>() {}
            );
            data.put("fetchedCategorys", billerResponseList);
            return new GlobalResponse(GlobalEnum.S0000.getValue(),
                    GlobalConstant.Categories_Fetched_Successfully,
                    data);
        } catch (Exception e){
            log.error("ERROR ::: Fetching category's failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Fetching_Categories,
                    data);

        }
    }

    public GlobalResponse deleteCategory(String categoryId) {
        Map<Object, Object> data = new HashMap<>();
        try {
            Category category = categoryRepository.findByCategoryId(categoryId);
            if (category != null) {
                categoryRepository.delete(category);
                data.put("deletedCategoryId", categoryId);
                return new GlobalResponse(
                        GlobalEnum.S0000.getValue(),
                        GlobalConstant.Category_Deleted_Successfully,
                        data
                );
            } else {
                return new GlobalResponse(
                        GlobalEnum.E0000.getValue(),
                        GlobalConstant.Error_Deleting_Category,
                        data
                );
            }
        } catch (Exception e) {
            log.error("ERROR ::: deleting category failed : {}", e.getMessage(), e);
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Deleting_Category,
                    data
            );
        }
    }

}
