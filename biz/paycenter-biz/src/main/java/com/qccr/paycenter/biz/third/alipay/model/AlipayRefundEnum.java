package com.qccr.paycenter.biz.third.alipay.model;

/**
 * 支付宝退款状态
 * author: denghuajun
 * date: 2016/4/5 17:32
 * version: v1.0.0
 */
public enum AlipayRefundEnum {


    WAIT_REFUND(0,"WAIT_ALIPAY_REFUND"), //等待支付宝退款
    REFUND_CLOSED(2,"REFUND_CLOSED"),    //退款关闭
    REFUND_SUCCESS(1,"REFUND_SUCCESS"),  //退款成功
    ACTIVE_REFUND(4,"ACTIVE_REFUND");    //退款进行中

    private int value ;
    private String name;

    AlipayRefundEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static AlipayRefundEnum valueOf(int value) {
        for(AlipayRefundEnum rcode : AlipayRefundEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static AlipayRefundEnum get(String name) {
        for(AlipayRefundEnum config : AlipayRefundEnum.values()){
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
