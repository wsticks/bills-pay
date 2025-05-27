package com.interswitch.user_management.controller;

import com.interswitch.user_management.model.request.BillerRequest;
import com.interswitch.user_management.model.request.UpdateBillerRequest;
import com.interswitch.user_management.model.response.GlobalResponse;
import com.interswitch.user_management.service.BillerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billers/")
public class BillerController {

    @Autowired
    private BillerService billerService;

    @PostMapping
    @PreAuthorize("hasRole('CAN_CREATE_BILLER')")
    public GlobalResponse createBiller(@RequestBody BillerRequest billerRequest) {
        return billerService.createBiller(billerRequest);
    }


    @PutMapping
    @PreAuthorize("hasRole('CAN_UPDATE_BILLER')")
    public GlobalResponse updateBiller(@RequestBody UpdateBillerRequest request,
                                       @RequestParam String productId) {
        return billerService.updateBiller(request,productId);
    }

    @GetMapping
    @PreAuthorize("hasRole('CAN_FETCH_BILLER')")
    public GlobalResponse fetchBiller(@RequestParam String billerId) {
        return billerService.fetchBiller(billerId);
    }

    @GetMapping("fetch_all")
    @PreAuthorize("hasRole('CAN_FETCH_BILLERS')")
    public GlobalResponse fetchBillers() {
        return billerService.fetchBillers();
    }

    @DeleteMapping("delete")
    @PreAuthorize("hasRole('CAN_DELETE_BILLERS')")
    public GlobalResponse deleteProduct(String billerId) {
        return billerService.deleteBiller(billerId);
    }
}
