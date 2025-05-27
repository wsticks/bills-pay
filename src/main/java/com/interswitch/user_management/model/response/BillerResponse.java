package com.interswitch.user_management.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class BillerResponse {

    private String billerId;
    private String billerName;
}
