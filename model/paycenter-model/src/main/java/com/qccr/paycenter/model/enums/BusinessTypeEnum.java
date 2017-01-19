package com.qccr.paycenter.model.enums;

/**
 * Created by lim on 2016/7/15.
 */
public enum BusinessTypeEnum {

    NORMAL(1,"normal","正常"),
    FUELCARD(2,"fuelcard","油卡"),
    CARD_COUPONS(3,"cardcoupons","卡券");

    private int value ;
    private String businessType;
    private String desc;
    private BusinessTypeEnum(int value, String businessType, String desc) {
        this.value = value;
        this.businessType = businessType;
        this.desc = desc;
    }

    public static BusinessTypeEnum valueOf(int value) {
        for(BusinessTypeEnum rcode : BusinessTypeEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static BusinessTypeEnum get(String msg) {
        for(BusinessTypeEnum config : BusinessTypeEnum.values()){
            if(config.desc.equals(msg))
                return config;
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getDesc() {
        return desc;
    }
}
