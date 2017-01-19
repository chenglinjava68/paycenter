package com.qccr.paycenter.biz.third.czbank.model;

/**
 * 浙商交易状态
 * author: lim
 * date: 2016/4/15 14:28
 * version: v1.0.0
 */
public enum CzbankPayEnum {
    SUCCESS(1,"SUCCESS"),  //消费成功
    REFUND(2,"REFUND"),    //退款成功
    UNKNOWN(9,"UNKNOWN"),   //结果未知,需要重新发起查询
    PAYERROR(0,"PAYERROR");  //交易失败

    private int value ;
    private String name;

    CzbankPayEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CzbankPayEnum valueOf(int value) {
        for(CzbankPayEnum rcode : CzbankPayEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static CzbankPayEnum get(String name) {
        for(CzbankPayEnum config : CzbankPayEnum.values()){
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
