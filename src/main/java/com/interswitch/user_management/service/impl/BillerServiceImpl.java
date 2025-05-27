package com.interswitch.user_management.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interswitch.user_management.constant.GlobalConstant;
import com.interswitch.user_management.enums.GlobalEnum;
import com.interswitch.user_management.model.entity.Biller;
import com.interswitch.user_management.model.entity.Category;
import com.interswitch.user_management.model.request.BillerRequest;
import com.interswitch.user_management.model.request.UpdateBillerRequest;
import com.interswitch.user_management.model.response.BillerResponse;
import com.interswitch.user_management.model.response.GlobalResponse;
import com.interswitch.user_management.repository.BillerRepository;
import com.interswitch.user_management.repository.CategoryRepository;
import com.interswitch.user_management.service.BillerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class BillerServiceImpl  implements BillerService {

    @Autowired
    private BillerRepository billerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public GlobalResponse createBiller(BillerRequest billerRequest) {
        Map<Object, Object> data = new HashMap<>();
        try {
            Category category = categoryRepository.findByCategoryId(billerRequest.getCategoryId());
                    if(category == null){
                        return new GlobalResponse(
                                GlobalEnum.E0000.getValue(),
                                "Category not found with ID: " + billerRequest.getCategoryId(),
                                data
                        );
                    }

            Biller biller = new Biller();
            biller.setBillerName(billerRequest.getBillerName());
            biller.setBillerId(UUID.randomUUID().toString());
            biller.setCategory(category);
            biller.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
            biller.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

            // 3. Save biller (only once)
            biller = billerRepository.save(biller);

            // 4. Convert to response DTO
            BillerResponse response = convertToBillerResponse(biller);

            data.put("biller", response);
            log.info("Success:::: Biller created successfully with ID: {}", biller.getBillerId());

            return new GlobalResponse(
                    GlobalEnum.S0000.getValue(),
                    GlobalConstant.Biller_Created_Successfully,
                    data
            );
        } catch (EntityNotFoundException e) {
            log.error("ERROR ::: Category not found: {}", e.getMessage());
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    "Associated category not found",
                    data
            );
        } catch (Exception e) {
            log.error("ERROR ::: Biller creation failed: {}", e.getMessage(), e);
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Creating_Biller,
                    data
            );
        }
    }

    private BillerResponse convertToBillerResponse(Biller biller) {
        BillerResponse response = new BillerResponse();
        response.setBillerId(biller.getBillerId());
        response.setBillerName(biller.getBillerName());

        // Include minimal category info if needed
        if (biller.getCategory() != null) {
            response.setCategoryId(biller.getCategory().getCategoryId());
            response.setCategoryName(biller.getCategory().getCategoryName());
        }

        return response;
    }

    public GlobalResponse updateBiller(UpdateBillerRequest request, String billerId) {
        Map<Object, Object> data = new HashMap<>();
        try {
            // Step 1: Retrieve existing biller
            Biller existingBiller = billerRepository.findByBillerId(billerId);
            if (existingBiller == null) {
                return new GlobalResponse(
                        GlobalEnum.E0000.getValue(),
                        GlobalConstant.Biller_Not_Found,
                        data
                );
            }
            existingBiller.setBillerName(request.getBillerName());
            existingBiller.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            Biller updatedBiller = billerRepository.save(existingBiller);
            BillerResponse response = objectMapper.convertValue(updatedBiller, BillerResponse.class);

            data.put("updatedBiller", response);
            log.info("Success ::: Biller updated successfully with response: {}", response);

            return new GlobalResponse(
                    GlobalEnum.S0000.getValue(),
                    GlobalConstant.Product_Updated_Successfully,
                    data
            );

        } catch (Exception e) {
            log.error("ERROR ::: Biller update failed : {}", e.getMessage(), e);
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Updating_Product,
                    data
            );
        }
    }

    public GlobalResponse fetchBiller(String billerId){
        Map<Object,Object> data = new HashMap<>();
        try{
            Biller fetchedBiller = billerRepository.findByBillerId(billerId);
            if (fetchedBiller != null) {
                BillerResponse billerResponse = new BillerResponse();
                billerResponse.setBillerId(fetchedBiller.getBillerId());
                billerResponse.setBillerName(fetchedBiller.getBillerName());
                billerResponse.setBillerId(fetchedBiller.getBillerId());
                billerResponse.setCategoryId(fetchedBiller.getCategory().getCategoryId());
                billerResponse.setCategoryName(fetchedBiller.getCategory().getCategoryName());
//
                data.put("fetchedBiller", billerResponse);
                return new GlobalResponse(GlobalEnum.S0000.getValue(),
                        GlobalConstant.Biller_Fetched_Successfully,
                        data);
            } else{
                return new GlobalResponse(GlobalEnum.E0000.getValue(),
                        GlobalConstant.Biller_Not_Found,
                        data);
            }
        } catch (Exception e){
            log.error("ERROR ::: product fetch failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Fetching_Product,
                    data);

        }
    }

    public GlobalResponse fetchBillers(){
        Map<Object,Object> data = new HashMap<>();
        try{
            List<Biller> fetchBillers = billerRepository.findAll();
            List<BillerResponse> billerResponseList = new ArrayList<>();

            data.put("fetchedBillers", fetchBillers);
            return new GlobalResponse(GlobalEnum.S0000.getValue(),
                    GlobalConstant.Billers_Fetched_Successfully,
                    data);
        } catch (Exception e){
            log.error("ERROR ::: product fetch failed : {} ", e.getMessage());
            return new GlobalResponse(GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Fetching_Billers,
                    data);

        }
    }

    public GlobalResponse deleteBiller(String billerId) {
        Map<Object, Object> data = new HashMap<>();
        try {
            Biller biller = billerRepository.findByBillerId(billerId);
            if (biller != null) {
                billerRepository.delete(biller);
                data.put("deletedBillerId", billerId);
                return new GlobalResponse(
                        GlobalEnum.S0000.getValue(),
                        GlobalConstant.Biller_Deleted_Successfully,
                        data
                );
            } else {
                return new GlobalResponse(
                        GlobalEnum.E0000.getValue(),
                        GlobalConstant.Error_Deleting_Biller,
                        data
                );
            }
        } catch (Exception e) {
            log.error("ERROR ::: deleting biller failed : {}", e.getMessage(), e);
            return new GlobalResponse(
                    GlobalEnum.E0000.getValue(),
                    GlobalConstant.Error_Deleting_Biller,
                    data
            );
        }
    }
}
