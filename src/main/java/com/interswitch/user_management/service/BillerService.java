package com.interswitch.user_management.service;

import com.interswitch.user_management.model.request.BillerRequest;
import com.interswitch.user_management.model.request.UpdateBillerRequest;
import com.interswitch.user_management.model.response.GlobalResponse;

public interface BillerService {

    GlobalResponse createBiller(BillerRequest billerRequest);

    GlobalResponse updateBiller(UpdateBillerRequest request, String billerId);

    GlobalResponse fetchBiller(String billerId);

    GlobalResponse fetchBillers();

    GlobalResponse deleteBiller(String billerId);




    }
