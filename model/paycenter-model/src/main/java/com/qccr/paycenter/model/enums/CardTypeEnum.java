package com.qccr.paycenter.model.enums;


/**
 * 銀行卡類型
 */
public enum CardTypeEnum {
    CREDIT_CARD(1,"creditcard","信用卡"),
    DEBIT_CARD(2,"debitcard","储蓄卡");

    private int value ;
    private String name;
    private String desc;
    private CardTypeEnum(int value, String name, String desc) {
        this.value = value;
        this.name = name;
        this.desc = desc;
    }

    public static CardTypeEnum valueOf(int value) {
        for(CardTypeEnum rcode : CardTypeEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static CardTypeEnum get(String msg) {
        for(CardTypeEnum config : CardTypeEnum.values()){
            if(config.desc.equals(msg))
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

    public String getDesc() {
        return desc;
    }
}
