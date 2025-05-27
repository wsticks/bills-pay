package com.interswitch.user_management.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class CustomPageable {

    public CustomPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        this.page = (page == null) ? 0 : page;
        this.size = (size == null) ? 50 : size;
        this.sortBy = (StringUtils.isEmpty(sortBy)) ? "id" : sortBy;
        this.sortDirection = (StringUtils.isEmpty(sortDirection)) ? "desc" : sortDirection;
    }

    private int page = 0;
    private int size = 50;
    private String sortBy = "id";
    private String sortDirection = "desc";
}
