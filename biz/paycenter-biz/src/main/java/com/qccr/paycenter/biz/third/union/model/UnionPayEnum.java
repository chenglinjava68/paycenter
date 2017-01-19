package com.qccr.paycenter.biz.third.union.model;

/**
 * author: denghuajun
 * date: 2016/4/6 19:18
 * version: v1.0.0
 */
public enum UnionPayEnum {
    NOPAY(0,"34"),  //无此交易，或者未支付
    SUCCESS(1,"00"),  //支付成功
    PAYERROR(2,"01"), //支付失败
    UNKNOWN(4,"04"), //支付状态未知,需要重新发起查询
    OUTTIME(5,"03"), //超时需要重新发起查询
    USERPAYING(3,"05"); //-用户支付中,需要重新发起查询



    private int value ;
    private String name;

    UnionPayEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static UnionPayEnum valueOf(int value) {
        for(UnionPayEnum target : UnionPayEnum.values()){
            if(target.value == value)
                return target;
        }
        return null;
    }

    public static UnionPayEnum get(String name) {
        for(UnionPayEnum target : UnionPayEnum.values()){
            if(target.name.equals(name))
                return target;
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
