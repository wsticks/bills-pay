package com.interswitch.user_management.enums;

public enum GlobalEnum {

    E0000("E0000"),

    S0000("S0000"),
    ;

    private String value;

    public String getValue() {
        return value;
    }

    GlobalEnum(final String val){
        this.value = val;
    }
}
