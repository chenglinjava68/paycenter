package com.qccr.paycenter.model.enums;

/**
 * author: denghuajun
 * date: 2016/3/7 18:02
 * version: v1.0.0
 */
public enum RegexEnum {
    IP(1, "ip"),
    PHONE(2,"phone"),
    URL(3,"url"),
    EMAIL(4,"email"),
    TELEPHONE(6,"telephone"),
    CARD(5,"card");
    private int value ;
    private String name;

    RegexEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static RegexEnum valueOf(int value) {
        for(RegexEnum rcode : RegexEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static RegexEnum get(String name) {
        for(RegexEnum config : RegexEnum.values()){
            if(config.name.equals(name))
                return config;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
