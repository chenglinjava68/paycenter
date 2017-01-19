package com.qccr.paycenter.biz.third.alipay.model;

/**
 * 支付宝支付状态
 * author: denghuajun
 * date: 2016/4/5 17:09
 * version: v1.0.0
 */
public enum AlipayPayEnum {

    WAIT_PAY(0,"WAIT_BUYER_PAY"),  //未支付
    TRADE_CLOSED(2,"TRADE_CLOSED"),//交易完毕，如果，存在退款状态，同步更新
    TRADE_SUCCESS(1,"TRADE_SUCCESS"),  //支付成功
    TRADE_FINISHED(3,"TRADE_FINISHED"); //交易完成结束


    private int value ;
    private String name;

    AlipayPayEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static AlipayPayEnum valueOf(int value) {
        for(AlipayPayEnum rcode : AlipayPayEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static AlipayPayEnum get(String name) {
        for(AlipayPayEnum config : AlipayPayEnum.values()){
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
