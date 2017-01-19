package com.qccr.paycenter.biz.third.wechat.model;

/**
 * 微信支付状态
 * author: denghuajun
 * date: 2016/4/1 14:28
 * version: v1.0.0
 */
public enum  WechatPayEnum {

    NOTPAY(0,"NOTPAY"),  //未支付
    SUCCESS(1,"SUCCESS"),  //支付成功
    REFUND(5,"REFUND"),    //转入退款
    CLOSED(4,"CLOSED"),    //已经关闭
    REVOKED(6,"REVOKED"),   //已撤销
    USERPAYING(3,"USERPAYING"), //-用户支付中
    PAYERROR(2,"PAYERROR");  //支付失败

    private int value ;
    private String name;

    WechatPayEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static WechatPayEnum valueOf(int value) {
        for(WechatPayEnum rcode : WechatPayEnum.values()){
            if(rcode.value == value)
                return rcode;
        }
        return null;
    }

    public static WechatPayEnum get(String name) {
        for(WechatPayEnum config : WechatPayEnum.values()){
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
